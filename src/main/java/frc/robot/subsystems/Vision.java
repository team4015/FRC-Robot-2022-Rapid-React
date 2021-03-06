/* ==================================================
 * Author: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 *
 * Contains functions needed to control the vision system.
 * ================================================== */

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.pipelines.*;

public class Vision extends SubsystemBase {
  // HARDWARE //
  private Solenoid light;
  // PORTS //
  private final static int LIGHT_PORT = 2;

  // CONSTANTS //
  final static int IMG_HEIGHT = 120;
  final static int IMG_WIDTH = 160;
  final static int FPS = 30;

  final static int TURN_THRESHOLD = 8;
   double SPEED_ADJUST = 1.08; //.96 for school

  private final static double PIXELS_TO_DEGREES = 0.35;
  private final static double TOLERANCE = 4.5;
  //private final static double BASE_TURN_SPEED = .3;
  //private final static double MIN_TURN_SPEED = 0.5;
  private final static double MAX_TURN_SPEED = 3;

  // VARIABLES //
  private VisionThread visionThread;
  private PipelineSettings settings;
  private double xCentre;
  private double width;
  private Object imgLock;
  private double shooterSpeed = 5;
  private double previousTurn;
  private double angleError;
  private double currentAngle;
  private double turnSpeed;
  private boolean aligned;
  private boolean inRange;
  private double adjustment = 10; //35
  private double overlayWidth = 30;
  private double height = 30;

  private SendableChooser<PipelineSettings> visionPipelines;
  private SendableChooser<Boolean> showRectangles;
  private SendableChooser<Boolean> showLines;
  private SendableChooser<VisionType> visionType;

  private boolean aimingLight;
  private boolean shootingLight;

  private PIDController pid;
  private double p = 0.02;
  private double i = 0.08;
  private double d = 0.001;

  private double estimateCoeff = 0.3
  ;

  SimpleMotorFeedforward feedForward;
  private final static double KS = 1.2374;
  private final static double KV = 5.6065;
  private final static double KA = 2;
  
  public Vision() {
    feedForward = new SimpleMotorFeedforward(KS, KV, KA);

    pid = new PIDController(p,i,d);
    pid.setTolerance(TOLERANCE);
    pid.setIntegratorRange(-.5, .5);

    SmartDashboard.putNumber("Speed Adjust",SPEED_ADJUST);
    SmartDashboard.putNumber("Inverse Height", height);
    SmartDashboard.putNumber("Adjust", adjustment);
    SmartDashboard.putNumber("Overlay Width", overlayWidth);
    SmartDashboard.putNumber("P", p);
    SmartDashboard.putNumber("I", i);
    SmartDashboard.putNumber("D", d);
    SmartDashboard.putNumber("Speedy", shooterSpeed);
    SmartDashboard.putNumber("Estimate Coeff", estimateCoeff);

    shooterSpeed = 0;
    xCentre = IMG_WIDTH/2.0;
    width = IMG_WIDTH/2.0;
    imgLock = new Object();
    light = new Solenoid(PneumaticsModuleType.CTREPCM, LIGHT_PORT);
    light.set(true); // turn light off

    previousTurn = 100000;
    angleError = 0;
    currentAngle = 0;
    inRange = false;

    visionPipelines = new SendableChooser<>();
    visionPipelines.setDefaultOption("Provincial Vision", new ProvincialSettings());
    visionPipelines.addOption("Highschool Vision", new LongSettings());
    visionPipelines.addOption("Practice field Vision", new StMarySettings());
    visionPipelines.addOption("Waterloo Vision", new WaterlooSettings());
    visionPipelines.addOption("Humber Vision", new HumberSettings());
    SmartDashboard.putData(visionPipelines);

    showRectangles = new SendableChooser<>();
    showRectangles.setDefaultOption("Show Boxes", true);
    showRectangles.addOption("Don't Show Boxes", false);
    SmartDashboard.putData(showRectangles);

    showLines = new SendableChooser<>();
    showLines.setDefaultOption("Show Lines", true);
    showLines.addOption("Don't Show Lines", false);
    SmartDashboard.putData(showLines);

    visionType = new SendableChooser<>();
    visionType.setDefaultOption("Long", VisionType.LONG);
    visionType.addOption("Pieces", VisionType.PIECES);
    visionType.addOption("Biggest Piece", VisionType.BIGGEST);
    visionType.addOption("Full Target", VisionType.FULL);
    visionType.addOption("Nothing", VisionType.NOTHING);
    SmartDashboard.putData(visionType);

    aimingLight = false;
    shootingLight = false;
  }

  // METHODS //

  public boolean isAligned() {
    return aligned;
  }

  public double getShooterSpeed() {
    return shooterSpeed;
  }

  public double getTurnSpeed() {
    if (Math.abs(turnSpeed) <= 3.5) return turnSpeed;
    else return Math.copySign(3.5, turnSpeed);
  }

  /* =====================================
  Author: Lucas Jacobs

  Desc:
  This method initalizes the camera and the vison
  thread so that frames from the camera can be accessed
  ===================================== */
  public void initCamera() {

    // initalize camera
    UsbCamera cam = CameraServer.startAutomaticCapture();

    cam.setResolution(IMG_WIDTH, IMG_HEIGHT);
    cam.setFPS(FPS);
    
    StandardPipeline standardPipeline = new StandardPipeline();
    settings = visionPipelines.getSelected();
    standardPipeline.set(settings);

    setExposure(cam, standardPipeline);

    CvSink vIn = CameraServer.getVideo();
    CvSource vOut = CameraServer.putVideo("Target Video", IMG_WIDTH, IMG_HEIGHT);
    CvSource vOutFilter = CameraServer.putVideo("Filtered", IMG_WIDTH, IMG_HEIGHT);

    // initialize pipeline filter settings
    outputFilterSettings(standardPipeline);

    // initalize vision thread

    visionThread = new VisionThread(cam, standardPipeline, pipeline -> {
      // Set to be the currently selected Pipeline
      synchronized (imgLock) {
        if (settings != visionPipelines.getSelected()) {
          settings = visionPipelines.getSelected();
          pipeline.set(settings);
          outputFilterSettings(pipeline);
        }

        // Retrieve new filter settings each time the thread runs
        retrieveFilterSettings(pipeline);
        setExposure(cam, pipeline);
      }

      //Create output frames which will have rectangles drawn on them
      Mat output = new Mat();
      Mat filtered = pipeline.maskOutput();
      vIn.grabFrame(output);

      SmartDashboard.putNumber("Targets Detected", pipeline.filterContoursOutput().size());

      if (!pipeline.filterContoursOutput().isEmpty()) {

        //Get biggest contour and find its centre

        ArrayList<Rect> targets = new ArrayList<Rect>();

        Rect biggest = new Rect(); // Initialized to 0 area rectangle

        for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
          Rect contour = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));

         //  if (contour.y + contour.height > .85*IMG_HEIGHT) continue; // Skip rectangles in the bottom fourth of the screen
          targets.add(contour);

          if (contour.area() > biggest.area()) biggest = contour;

          // Show contours in green
          synchronized (imgLock) {
            if (showRectangles.getSelected()) {
              Imgproc.rectangle(output, contour,  new Scalar(0, 255, 0, 255), 1);
            }
          }
        }
        // ---------------------------------------------
        //Show biggest contour in red
        synchronized (imgLock) {
          if (showRectangles.getSelected()) {
            Imgproc.rectangle(output, biggest,  new Scalar(0, 0, 255, 255), 1);
          }
        }

        ArrayList<Rect> targetRects = new ArrayList<>();
        LinkedList<Rect> checkThese = new LinkedList<Rect>();
        boolean firstLoop = true;
        
        while (targets.size() > 0) {
          Rect targetRect = new Rect();

          if (firstLoop) {
            targetRect = biggest;
            firstLoop = false;
          } else {
            targetRect = targets.get(0);
          } 
          checkThese.add(targetRect);
        while (checkThese.size() > 0) { // Go through rectangles in the target
          Rect checked = checkThese.pop();

          Point checkedCentre = new Point(checked.x + checked.width/2, checked.y + checked.height/2);

          for (int i = 0; i < targets.size(); i++) { //Go through rectangles not yet in target
            Rect potential = targets.get(i);

            Point potentialCentre = new Point(potential.x + potential.width/2, potential.y + potential.height/2);

            //if potential rect is in target
            if (Math.abs(potentialCentre.x - checkedCentre.x) < 30 && Math.abs(potentialCentre.y - checkedCentre.y) < 12) {
              checkThese.add(potential);
              targets.remove(i);
              i--;

              //*********Add potential to target Rect***************

              int x = targetRect.x;
              int y = targetRect.y;
              int width = targetRect.width;
              int height = targetRect.height;

              //set left

              targetRect.x = Math.min(x, potential.x);
              //set top
              targetRect.y = Math.min(y, potential.y);
              //set right
              targetRect.width = Math.max(x + width, potential.x + potential.width) - targetRect.x;
              //set bott
              targetRect.height = Math.max(y + height, potential.y + potential.height) - targetRect.y;
            }
          }
        }
        targetRects.add(targetRect);
        }

     Rect targetRect = biggest;

      for (int i = 0; i < targetRects.size(); i++) {
        Rect contour = targetRects.get(i);

        synchronized (imgLock) {
          if (showRectangles.getSelected()) {
            Imgproc.rectangle(output, contour,  new Scalar(0xE6, 0xD8, 0xAD, 255), 1);
          }
        }

        if (contour.y + contour.height > .85*IMG_HEIGHT) continue; // Skip rectangles in the bottom fourth of the screen
        if (contour.area() > 1600) continue;
        if (contour.width > targetRect.width) targetRect = contour;

        // Show contours in green
        //synchronized (imgLock) {
          //if (showRectangles.getSelected()) {
            //Imgproc.rectangle(output, contour,  new Scalar(0, 255, 255, 255), 1);
        //}
      }


        //-----------------------------------------------------

        synchronized (imgLock) {
          if (showRectangles.getSelected()) {
            //Add target in blue on screen
            Imgproc.rectangle(output, targetRect,  new Scalar(255, 0, 0, 255), 1);

            // Show shooting lines on screen

            this.overlayWidth = SmartDashboard.getNumber("Overlay Width", overlayWidth);
            this.adjustment = SmartDashboard.getNumber("Adjust", adjustment);
            this.height = SmartDashboard.getNumber("Inverse Height",height);
            double adjustment = IMG_WIDTH/2.0 -this.adjustment;
            /*
            WIDTH   SPEED
            55      5.2 RED
            58      4.8 ORANGE
            60      5 -----------------
            65      4.5 GREEN
            70      4.4 BLUE
            80      4.3 VIOLET

            24     4.3 REd
            33     4.3 or
            49     4.4 gg
            68     4.6 b
            80     5   v

            */
          }

          Imgproc.line(output, new Point(adjustment, 0), new Point(adjustment, 40), new Scalar(0,0,0));
            /*horizLineOnScreen(output, height, new Scalar(0, 0, 0));
            horizLineOnScreen(output, 33, new Scalar(0x22, 0, 0xE3)); //BGR //RED
            horizLineOnScreen(output, 45, new Scalar(0, 0x7E, 0xFF));
            horizLineOnScreen(output, 57, new Scalar(0x08, 0x88, 0x13));
            horizLineOnScreen(output, 77, new Scalar(0xBA, 0x48, 0));
            horizLineOnScreen(output, 90, new Scalar(0x85, 0x15, 0xC7)); //VIOLET*/

          this.xCentre = targetRect.x + (targetRect.width / 2); //Set the centre of the bounding rectangle
          this.width = targetRect.width;
          this.height = targetRect.y;
          inRange = width > 9 && width < 80;
          SmartDashboard.putNumber("Width", biggest.width);
          SmartDashboard.putNumber("Target Width", width);
          SmartDashboard.putNumber("Target Height", targetRect.y);
          SmartDashboard.putBoolean("In Shooting Range", inRange);
          SmartDashboard.putNumber("Centre (0 to 1) ", xCentre/160.0);
          autoShooterSpeed(); //Prints the speed needed to get the ball in to the dashboard
        }
      }


      synchronized (imgLock) {
        if (showLines.getSelected()) {
      //Imgproc.line(output, new Point(adjustment, 0), new Point(adjustment, 40), new Scalar(0,0,0));
            //horizLineOnScreen(output, height, new Scalar(0, 0, 0));
            horizLineOnScreen(output, 20, new Scalar(0x22, 0, 0xE3)); //BGR //RED
            horizLineOnScreen(output, 35, new Scalar(0, 0x7E, 0xFF));
            horizLineOnScreen(output, 50, new Scalar(0x08, 0x88, 0x13));
            horizLineOnScreen(output, 65, new Scalar(0xBA, 0x48, 0));
            horizLineOnScreen(output, 80, new Scalar(0x85, 0x15, 0xC7)); //VIOLET
        }
          }

      vOut.putFrame(output);

      //Put out rbg or hsv filter output depending on pipeline settings
       vOutFilter.putFrame(filtered);
    });

    visionThread.start();
  }

  public void lineOnScreen(Mat output, double totalWidth, Scalar colour) {
    double offset = totalWidth/2;
    double middle = IMG_WIDTH/2.0-adjustment;
    Imgproc.line(output, new Point(middle+offset, 0), new Point(middle+offset, 120), colour);
    Imgproc.line(output, new Point(middle-offset, 0), new Point(middle-offset, 120), colour);
  }

  public void horizLineOnScreen(Mat output, double height, Scalar colour) {
    Imgproc.line(output, new Point(0, height), new Point(160, height), colour);
  }
  /* =====================================
  Author: Lucas Jacobs

  Desc:
  This method returns the amount that the robot should turn
  in order to aim at the target
  ===================================== */
  public double aimAtTarget() {
    if (!inRange) return 0;
    double xCentre;
    synchronized (imgLock) {
      xCentre = this.xCentre;
    }

    double turn = xCentre - (IMG_WIDTH/ 2.0)+adjustment;
    SmartDashboard.putNumber("Dist to Target", turn);

    //return (2)
  //  if (height < 60) {
     return turn; // return difference between the target and where the robot is pointed
    // } else {
 //   return Math.
 // }
  }

  /* =====================================
  Author: Lucas Jacobs

  Desc:
  This method returns the speed (in volts) the shooter should spin to get in the target
  ===================================== */
  public double autoShooterSpeed() {

    double x;
    synchronized (imgLock) {
      x = this.height;
    }

    SmartDashboard.putNumber("Gotten Height", x);
    // Function to supply volts to the shooter (using Excel)
    shooterSpeed = 0.000215681005927434*x*x - 0.0105548924930645*x + 4.4241301579637;

    SPEED_ADJUST = SmartDashboard.getNumber("Speed Adjust", SPEED_ADJUST);
    shooterSpeed *= SPEED_ADJUST;
    SmartDashboard.putNumber("Shooter Speed", shooterSpeed);
    SmartDashboard.putNumber("Speedy", shooterSpeed);

    return shooterSpeed; // return difference between the target and where the robot is pointed
  }

  /* ==========================
  * Author: Lucas Jacobs
  * Desc: Enables the ring light 
  * for aiming
  * ===========================*/
  public void enableAimingLight() {
    aimingLight = true;
    light.set(true);
  }

  /* ==========================
  * Author: Lucas Jacobs
  * Desc: Disables the ring light 
  * as long as shooting doesn't need it
  * ===========================*/
  public void disableAimingLight() {
    aimingLight = false;
    light.set(aimingLight || shootingLight);
  }

  /* ==========================
  * Author: Lucas Jacobs
  * Desc: Enables the ring light 
  * for shooting
  * ===========================*/
  public void enableShootingLight() {
    shootingLight = true;
    light.set(true);
  }

  /* ==========================
  * Author: Lucas Jacobs
  * Desc: Disables the ring light
  * as long as aiming doesn't need it 
  * ===========================*/
  public void disableShootingLight() {
    shootingLight = false;
    light.set(aimingLight || shootingLight);
  }

  /* ===================================================
  * Author: Lucas Jacobs
  *
  * Desc: Writes the RGB/HSV filter and contour filter
  * settings of the given pipeline to the SmartDashboard 
  * ====================================================*/

  private void outputFilterSettings(StandardPipeline pipeline) {
    // Write RGB/HSV filter values to dashboard
      SmartDashboard.putNumber("Upper Red", pipeline.rgbThresholdRed[1]);
      SmartDashboard.putNumber("Lower Red", pipeline.rgbThresholdRed[0]);
      SmartDashboard.putNumber("Upper Green", pipeline.rgbThresholdGreen[1]);
      SmartDashboard.putNumber("Lower Green", pipeline.rgbThresholdGreen[0]);
      SmartDashboard.putNumber("Upper Blue", pipeline.rgbThresholdBlue[1]);
      SmartDashboard.putNumber("Lower Blue", pipeline.rgbThresholdBlue[0]);

      SmartDashboard.putNumber("Upper Hue", pipeline.hsvThresholdHue[1]);
      SmartDashboard.putNumber("Lower Hue", pipeline.hsvThresholdHue[0]);
      SmartDashboard.putNumber("Upper Saturation", pipeline.hsvThresholdSaturation[1]);
      SmartDashboard.putNumber("Lower Saturation", pipeline.hsvThresholdSaturation[0]);
      SmartDashboard.putNumber("Upper Value", pipeline.hsvThresholdValue[1]);
      SmartDashboard.putNumber("Lower Value", pipeline.hsvThresholdValue[0]);

    //Write Contour filter values to dashboard
    SmartDashboard.putNumber("Min Area", pipeline.filterContoursMinArea);
    SmartDashboard.putNumber("Min Width", pipeline.filterContoursMinWidth);
    SmartDashboard.putNumber("Max Width", pipeline.filterContoursMaxWidth);
    SmartDashboard.putNumber("Min Height", pipeline.filterContoursMinHeight);
    SmartDashboard.putNumber("Max Height", pipeline.filterContoursMaxHeight);
    SmartDashboard.putNumber("Upper Solidity", pipeline.filterContoursSolidity[1]);
    SmartDashboard.putNumber("Lower Solidity", pipeline.filterContoursSolidity[0]);
    SmartDashboard.putNumber("Min Ratio", pipeline.filterContoursMinRatio);
    SmartDashboard.putNumber("Max Ratio", pipeline.filterContoursMaxRatio);

    SmartDashboard.putNumber("Exposure", pipeline.cameraExposure);
  }

  /* ===================================================
  * Author: Lucas Jacobs
  *
  * Desc: Gets the RGB/HSV filter and contour filter
  * settings from the dashboard and writes them to the pipeline.
  * This gives a much faster way of changing the pipeline settings
  * than changing them in the code directly and redeploying
  * ====================================================*/

  private void retrieveFilterSettings(StandardPipeline pipeline) {
      pipeline.rgbThresholdRed[1] = SmartDashboard.getNumber("Upper Red", pipeline.rgbThresholdRed[1]);
      pipeline.rgbThresholdRed[0] = SmartDashboard.getNumber("Lower Red", pipeline.rgbThresholdRed[0]);
      pipeline.rgbThresholdGreen[1] = SmartDashboard.getNumber("Upper Green", pipeline.rgbThresholdGreen[1]);
      pipeline.rgbThresholdGreen[0] = SmartDashboard.getNumber("Lower Green", pipeline.rgbThresholdGreen[0]);
      pipeline.rgbThresholdBlue[1] = SmartDashboard.getNumber("Upper Blue", pipeline.rgbThresholdBlue[1]);
      pipeline.rgbThresholdBlue[0] = SmartDashboard.getNumber("Lower Blue", pipeline.rgbThresholdBlue[0]);

      pipeline.hsvThresholdHue[1] = SmartDashboard.getNumber("Upper Hue", pipeline.hsvThresholdHue[1]);
      pipeline.hsvThresholdHue[0] = SmartDashboard.getNumber("Lower Hue", pipeline.hsvThresholdHue[0]);
      pipeline.hsvThresholdSaturation[1] = SmartDashboard.getNumber("Upper Saturation", pipeline.hsvThresholdSaturation[1]);
      pipeline.hsvThresholdSaturation[0] = SmartDashboard.getNumber("Lower Saturation", pipeline.hsvThresholdSaturation[0]);
      pipeline.hsvThresholdValue[1] = SmartDashboard.getNumber("Upper Value", pipeline.hsvThresholdValue[1]);
      pipeline.hsvThresholdValue[0] = SmartDashboard.getNumber("Lower Value", pipeline.hsvThresholdValue[0]);

    pipeline.filterContoursMinArea = SmartDashboard.getNumber("Min Area", pipeline.filterContoursMinArea);
    pipeline.filterContoursMinWidth = SmartDashboard.getNumber("Min Width", pipeline.filterContoursMinWidth);
    pipeline.filterContoursMaxWidth = SmartDashboard.getNumber("Max Width", pipeline.filterContoursMaxWidth);
    pipeline.filterContoursMinHeight = SmartDashboard.getNumber("Min Height", pipeline.filterContoursMinHeight);
    pipeline.filterContoursMaxHeight = SmartDashboard.getNumber("Max Height", pipeline.filterContoursMaxHeight);
    pipeline.filterContoursSolidity[1] = SmartDashboard.getNumber("Upper Solidity", pipeline.filterContoursSolidity[1]);
    pipeline.filterContoursSolidity[0] = SmartDashboard.getNumber("Lower Solidity", pipeline.filterContoursSolidity[0]);
    pipeline.filterContoursMinRatio = SmartDashboard.getNumber("Min Ratio", pipeline.filterContoursMinRatio);
    pipeline.filterContoursMaxRatio = SmartDashboard.getNumber("Max Ratio", pipeline.filterContoursMaxRatio);

    pipeline.cameraExposure = (int) SmartDashboard.getNumber("Exposure", pipeline.cameraExposure);
  }

  /* ===================================================
  * Author: Lucas Jacobs
  *
  * Desc:Sets the camera's exposure based on the given value
  * from the pipeline. If the exposure is out of range, it
  * sets the exposure to be auto.
  * ====================================================*/

  private void setExposure(UsbCamera cam, StandardPipeline pipeline) {
    int exposure = pipeline.cameraExposure;

    if (exposure >= 0 && exposure <= 100) {
      cam.setExposureManual(exposure);
    } else {
      cam.setExposureAuto();
    }
  }

  /* ===================================================
  * Author: Lucas Jacobs      Date: 31 March 2022
  *
  * Desc: Calculates what angle the robot is from the target
  * using frames from the camera. If the function is called multiple times
  * during the same frame, it uses the gyroscope to estimate how far it has turned
  * ====================================================*/
  public void calcAlign(double angle) {
    double turn = aimAtTarget(); //Get the turn speed from the camera
    if (turn == previousTurn) {
      angleError +=  angle - currentAngle; 
      currentAngle = angle;
      double pidAdjustment = pid.calculate(currentAngle);
      double estimate = 0;
      if (Math.abs(angleError) > TOLERANCE*.75) { estimate = Math.copySign(estimateCoeff, turn);
      turnSpeed = pidAdjustment + feedForward.calculate(estimate);}
   // turnSpeed = Math.copySign(2.6, turn);}
      else turnSpeed = 0;
    } else {
      previousTurn = turn;
      angleError = turn*PIXELS_TO_DEGREES;
      currentAngle = angle;
      double pidAdjustment = pid.calculate(currentAngle, currentAngle + angleError);
      double estimate = 0;
     if (Math.abs(angleError) > TOLERANCE*.75) {estimate = Math.copySign(estimateCoeff, turn);
     turnSpeed = pidAdjustment +  feedForward.calculate(estimate); /*turnSpeed = Math.copySign(2.6, turn);*/} // get new PID value and change the setpoint
      else turnSpeed = 0;
    }

    aligned = Math.abs(angleError) < TOLERANCE && inRange;
    SmartDashboard.putNumber("Error", angleError);
    SmartDashboard.putNumber("PID Speed", turnSpeed);
    SmartDashboard.putBoolean("ALIGNED", aligned);
  }

 /* ====================================================
  * Author: Lucas Jacobs      Date: 1 April 2022
  *
  * Desc: Resets the PID Controller and sets previousTurn to a value
  * that is unwritable by the code so that the pid gets a new setpoint
  * the next time that calcAlign runs
  * ====================================================*/
  public void resetPID() {
    pid.reset();

    p = SmartDashboard.getNumber("P", p);
    i = SmartDashboard.getNumber("I", i);
    d = SmartDashboard.getNumber("D", d);
    estimateCoeff = SmartDashboard.getNumber("Estimate Coeff", estimateCoeff);

    pid.setPID(p,i,d);

    previousTurn = 1000000000;
  }
}

enum VisionType {
  LONG,
  PIECES,
  BIGGEST,
  FULL,
  NOTHING;
}