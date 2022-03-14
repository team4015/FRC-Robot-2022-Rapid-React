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
  final static int CAM_HEIGHT = 480;
  final static int CAM_WIDTH = 640;
  final static int IMG_HEIGHT = 120;
  final static int IMG_WIDTH = 160;
  final static int FPS = 30;

  final static int TURN_THRESHOLD = 5;

  // VARIABLES //
  private VisionThread visionThread;
  private double xCentre;
  private double width;
  private Object imgLock;

  private SendableChooser<PipelineTemplate> visionPipelines;
  private SendableChooser<Boolean> showRectangles;

  private boolean aimingLight;
  private boolean shootingLight;


  public Vision() {
    xCentre = IMG_WIDTH/2.0;
    width = IMG_WIDTH/2.0;
    imgLock = new Object();
    light = new Solenoid(PneumaticsModuleType.CTREPCM, LIGHT_PORT);
    light.set(false); // turn light off

    visionPipelines = new SendableChooser<>();
    visionPipelines.setDefaultOption("School Vision", new SchoolPipeline());
    visionPipelines.addOption("Humber Vision", new HumberPipeline());
    visionPipelines.addOption("Test Vision", new TestPipeline());
    SmartDashboard.putData(visionPipelines);

    showRectangles = new SendableChooser<>();
    showRectangles.setDefaultOption("Show Rectangles", true);
    showRectangles.addOption("Don't Show Rectangles", false);
    SmartDashboard.putData(showRectangles);

    aimingLight = false;
    shootingLight = false;
  }

  // METHODS //

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
    //cam.setExposureAuto();
    cam.setExposureManual(25);

    CvSink vIn = CameraServer.getVideo();
    CvSource vOut = CameraServer.putVideo("Target Video", IMG_WIDTH, IMG_HEIGHT);
    CvSource vOutFilter = CameraServer.putVideo("Filtered", IMG_WIDTH, IMG_HEIGHT);

    // initialize pipeline filter settings
    outputFilterSettings(visionPipelines.getSelected());

    // initalize vision thread
    visionThread = new VisionThread(cam, visionPipelines.getSelected(), pipeline -> {
      
      // Set to be the currently selected Pipeline
      synchronized (imgLock) {
        if (pipeline != visionPipelines.getSelected()) {
          pipeline = visionPipelines.getSelected();
          outputFilterSettings(pipeline);
        }

        // Retirieve new filter settings each time the thread runs
        retrieveFilterSettings(pipeline);
      }

      //Create output frames which will have rectangles drawn on them
      Mat output = new Mat();
      vIn.grabFrame(output);

      SmartDashboard.putNumber("Targets Detected", pipeline.filterContoursOutput().size());

      if (!pipeline.filterContoursOutput().isEmpty()) {

        //Get biggest contour and find its centre

        ArrayList<Rect> targets = new ArrayList<Rect>();

        Rect biggest = new Rect(); // Initialized to 0 area rectangle

        for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
          Rect contour = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));

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
        LinkedList<Rect> checkThese = new LinkedList<Rect>();

        checkThese.add(biggest);

        Rect targetRect = biggest.clone();

        //Show biggest contour in red
        synchronized (imgLock) {
          if (showRectangles.getSelected()) {
            Imgproc.rectangle(output, biggest,  new Scalar(0, 0, 255, 255), 1);
          }
        }

        while (checkThese.size() > 0) { // Go through rectangles in the target
          Rect checked = checkThese.pop();

          Point checkedCentre = new Point(checked.x + checked.width/2, checked.y + checked.height/2);

          for (int i = 0; i < targets.size(); i++) { //Go through triangle not yet in target
            Rect potential = targets.get(i);

            Point potentialCentre = new Point(potential.x + potential.width/2, potential.y + potential.height/2);

            //if potential rect is in target
            if (Math.abs(potentialCentre.x - checkedCentre.x) < 40 && Math.abs(potentialCentre.y - checkedCentre.y) < 20) {
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
        //-----------------------------------------------------

        //Add target in blue on screen
        synchronized (imgLock) {
          if (showRectangles.getSelected()) {
            Imgproc.rectangle(output, targetRect,  new Scalar(255, 0, 0, 255), 1);
          }
        }


        synchronized (imgLock) {
          xCentre = targetRect.x + (targetRect.width / 2); //Set the centre of the bounding rectangle
          width = targetRect.width;
          SmartDashboard.putNumber("Width", width);
          SmartDashboard.putBoolean("In Shooting Range", width >= 4 && width <= 9);
          SmartDashboard.putNumber("Centre (0 to 1) ", xCentre/160.0);
          autoShooterSpeed(); //Prints the speed needed to get the ball in to the dashboard
        }
      }
      vOut.putFrame(output);

      //Put out rbg or hsv filter output depending on pipeline settings
      if (pipeline.isRGB) {
        vOutFilter.putFrame(pipeline.rgbThresholdOutput());
      } else {
        vOutFilter.putFrame(pipeline.hsvThresholdOutput());
      }
    });

    visionThread.start();
  }

  /* =====================================
  Author: Lucas Jacobs

  Desc:
  This method returns the amount that the robot should turn
  in order to aim at the target
  ===================================== */
  public double aimAtTarget() {
    double xCentre;
    synchronized (imgLock) {
      xCentre = this.xCentre;
    }

    double turn = xCentre - (IMG_WIDTH/ 2.0);
    SmartDashboard.putNumber("Dist to Target", turn);

    // If the robot is within the turn threshold of pointing straight at the target, it wil stop turning
    if (Math.abs(turn) < TURN_THRESHOLD) {
      turn = 0;
      SmartDashboard.putBoolean("ALIGNED", true);
    } else {
      SmartDashboard.putBoolean("ALIGNED", false);
    }

    return turn; // return difference between the target and where the robot is pointed
  }

  /* =====================================
  Author: Lucas Jacobs

  Desc:
  This method returns the speed the shooter should spin to get in the target
  ===================================== */
  public double autoShooterSpeed() {
    double width;
    synchronized (imgLock) {
      width = this.width;
    }

    double speed = 0; // PUT SOME FUNCTION INVOLVING WIDTH HERE

    if (width == 4) speed = 0.48;
    else if (width >= 5 && width <=  9) speed = -0.01333*width + 0.50666; //Experimentally Determined

    SmartDashboard.putNumber("Shooter Speed", speed);

    return speed; // return difference between the target and where the robot is pointed
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

  private void outputFilterSettings(PipelineTemplate pipeline) {
    // Write RGB/HSV filter values to dashboard
    if (pipeline.isRGB) {
      SmartDashboard.putNumber("Upper Red", pipeline.rgbThresholdRed[1]);
      SmartDashboard.putNumber("Lower Red", pipeline.rgbThresholdRed[0]);
      SmartDashboard.putNumber("Upper Green", pipeline.rgbThresholdGreen[1]);
      SmartDashboard.putNumber("Lower Green", pipeline.rgbThresholdGreen[0]);
      SmartDashboard.putNumber("Upper Blue", pipeline.rgbThresholdBlue[1]);
      SmartDashboard.putNumber("Lower Blue", pipeline.rgbThresholdBlue[0]);

      SmartDashboard.putNumber("Upper Hue", -1);
      SmartDashboard.putNumber("Lower Hue", -1);
      SmartDashboard.putNumber("Upper Saturation", -1);
      SmartDashboard.putNumber("Lower Saturation", -1);
      SmartDashboard.putNumber("Upper Value", -1);
      SmartDashboard.putNumber("Lower Value", -1);
    } else {
      SmartDashboard.putNumber("Upper Red", -1);
      SmartDashboard.putNumber("Lower Red", -1);
      SmartDashboard.putNumber("Upper Green", -1);
      SmartDashboard.putNumber("Lower Green", -1);
      SmartDashboard.putNumber("Upper Blue", -1);
      SmartDashboard.putNumber("Lower Blue", -1);

      SmartDashboard.putNumber("Upper Hue", pipeline.hsvThresholdHue[1]);
      SmartDashboard.putNumber("Lower Hue", pipeline.hsvThresholdHue[0]);
      SmartDashboard.putNumber("Upper Saturation", pipeline.hsvThresholdSaturation[1]);
      SmartDashboard.putNumber("Lower Saturation", pipeline.hsvThresholdSaturation[0]);
      SmartDashboard.putNumber("Upper Value", pipeline.hsvThresholdValue[1]);
      SmartDashboard.putNumber("Lower Value", pipeline.hsvThresholdValue[0]);
    }

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
  }

  /* ===================================================
  * Author: Lucas Jacobs
  *
  * Desc: Gets the RGB/HSV filter and contour filter
  * settings from the dashboard and writes them to the pipeline.
  * This gives a much faster way of changing the pipeline settings
  * than changing them in the code directly and redeploying
  * ====================================================*/

  private void retrieveFilterSettings(PipelineTemplate pipeline) {
    if (pipeline.isRGB) {
      pipeline.rgbThresholdRed[1] = SmartDashboard.getNumber("Upper Red", pipeline.rgbThresholdRed[1]);
      pipeline.rgbThresholdRed[0] = SmartDashboard.getNumber("Lower Red", pipeline.rgbThresholdRed[0]);
      pipeline.rgbThresholdGreen[1] = SmartDashboard.getNumber("Upper Green", pipeline.rgbThresholdGreen[1]);
      pipeline.rgbThresholdGreen[0] = SmartDashboard.getNumber("Lower Green", pipeline.rgbThresholdGreen[0]);
      pipeline.rgbThresholdBlue[1] = SmartDashboard.getNumber("Upper Blue", pipeline.rgbThresholdBlue[1]);
      pipeline.rgbThresholdBlue[0] = SmartDashboard.getNumber("Lower Blue", pipeline.rgbThresholdBlue[0]);
    } else {
      pipeline.hsvThresholdHue[1] = SmartDashboard.getNumber("Upper Hue", pipeline.hsvThresholdHue[1]);
      pipeline.hsvThresholdHue[0] = SmartDashboard.getNumber("Lower Hue", pipeline.hsvThresholdHue[0]);
      pipeline.hsvThresholdSaturation[1] = SmartDashboard.getNumber("Upper Saturation", pipeline.hsvThresholdSaturation[1]);
      pipeline.hsvThresholdSaturation[0] = SmartDashboard.getNumber("Lower Saturation", pipeline.hsvThresholdSaturation[0]);
      pipeline.hsvThresholdValue[1] = SmartDashboard.getNumber("Upper Value", pipeline.hsvThresholdValue[1]);
      pipeline.hsvThresholdValue[0] = SmartDashboard.getNumber("Lower Value", pipeline.hsvThresholdValue[0]);
    }

    pipeline.filterContoursMinArea = SmartDashboard.getNumber("Min Area", pipeline.filterContoursMinArea);
    pipeline.filterContoursMinWidth = SmartDashboard.getNumber("Min Width", pipeline.filterContoursMinWidth);
    pipeline.filterContoursMaxWidth = SmartDashboard.getNumber("Max Width", pipeline.filterContoursMaxWidth);
    pipeline.filterContoursMinHeight = SmartDashboard.getNumber("Min Height", pipeline.filterContoursMinHeight);
    pipeline.filterContoursMaxHeight = SmartDashboard.getNumber("Max Height", pipeline.filterContoursMaxHeight);
    pipeline.filterContoursSolidity[1] = SmartDashboard.getNumber("Upper Solidity", pipeline.filterContoursSolidity[1]);
    pipeline.filterContoursSolidity[0] = SmartDashboard.getNumber("Lower Solidity", pipeline.filterContoursSolidity[0]);
    pipeline.filterContoursMinRatio = SmartDashboard.getNumber("Min Ratio", pipeline.filterContoursMinRatio);
    pipeline.filterContoursMaxRatio = SmartDashboard.getNumber("Max Ratio", pipeline.filterContoursMaxRatio);
  }
}