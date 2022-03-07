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
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  // HARDWARE //

  // PORTS //

  // CONSTANTS //
  final static int CAM_HEIGHT = 480;
  final static int CAM_WIDTH = 640;
  final static int IMG_HEIGHT = 120;
  final static int IMG_WIDTH = 160;
  final static int FPS = 30;

  final static int TURN_THRESHOLD = 20;

  // VARIABLES //
  private VisionThread visionThread;
  private double xCentre;
  private double width;
  private Object imgLock;


  public Vision() {
    xCentre = IMG_WIDTH/2.0;
    width = IMG_WIDTH/2.0;
    imgLock = new Object();
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

    CvSink vIn = CameraServer.getVideo();
    CvSource vOut = CameraServer.putVideo("Target Video", IMG_WIDTH, IMG_HEIGHT);
    CvSource vOutFilter = CameraServer.putVideo("Filtered", IMG_WIDTH, IMG_HEIGHT);

    // initalize vision thread
    visionThread = new VisionThread(cam, new GripPipeline(), pipeline -> {
      
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

          //Paritial code to exclude contours around dark
          //Point contourCentre = new Point(contour.y + contour.height/2, contour.x+contour.width/2);

          //if (pipeline.rgbThresholdOutput().get((int) contourCentre.x, (int) contourCentre.y) == )

          if (contour.width > biggest.width) biggest = contour;

          Imgproc.rectangle(output, contour,  new Scalar(0, 255, 0, 255), 1); // Add rectangle to the output
        }
        // --------------------------------------------- 
        LinkedList<Rect> checkThese = new LinkedList<Rect>();

        checkThese.add(biggest);

        Rect targetRect = new Rect(biggest.x, biggest.y, biggest.width, biggest.height);

        //Biggest in red
        Imgproc.rectangle(output, biggest,  new Scalar(0, 0, 255, 255), 1);

        while (checkThese.size() > 0) { // Go through rectangles in the taRGET
          Rect checked = checkThese.pop();

          for (int i = 0; i < targets.size(); i++) { //go THROUGH RECTANGLES NOT YET IN TARGET
            Rect potential = targets.get(i);

            //if potential rect is in target
            if (Math.abs(potential.x - checked.x) < 13 && Math.abs(potential.y - checked.y) < 7) {
              checkThese.add(potential);
              targets.remove(i);
              i--;

              //*********Add potential to target Rect***************
              //set left

              targetRect.x = Math.min(targetRect.x, potential.x);
              //set top
              targetRect.y = Math.min(targetRect.y, potential.y);
              //set right
              targetRect.width = Math.max(targetRect.x + targetRect.width, potential.x + potential.width) - targetRect.x;
              //set bott
              targetRect.height = Math.max(targetRect.y + targetRect.height, potential.y + potential.height) - targetRect.y;
            }
          }
        }
        //-----------------------------------------------------

        //Add target blue in red on screen
        Imgproc.rectangle(output, targetRect,  new Scalar(255, 0, 0, 255), 1);


        synchronized (imgLock) {
          xCentre = targetRect.x + (targetRect.width / 2); //Set the centre of the bounding rectangle
          width = biggest.width;
          SmartDashboard.putNumber("Width", width);
          SmartDashboard.putBoolean("In Shooting Range", width >= 4 && width <= 9);
          SmartDashboard.putNumber("Centre (0 to 1) ", xCentre/160.0);
        }
      }
      vOut.putFrame(output);
      vOutFilter.putFrame(pipeline.rgbThresholdOutput());
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
}
