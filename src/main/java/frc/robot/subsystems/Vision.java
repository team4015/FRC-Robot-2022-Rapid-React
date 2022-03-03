/* ==================================================
 * Author: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 *
 * Contains functions needed to control the vision system.
 * ================================================== */

package frc.robot.subsystems;

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

    CvSink vIn = CameraServer.getVideo();
    CvSource vOut = CameraServer.putVideo("Target Video", IMG_WIDTH, IMG_HEIGHT);

    cam.setResolution(CAM_WIDTH, CAM_HEIGHT);
    cam.setFPS(FPS);

    // initalize vision thread
    visionThread = new VisionThread(cam, new GripPipeline(), pipeline -> {
      
      //Create output frames which will have rectangles drawn on them
      Mat output = new Mat();
      vIn.grabFrame(output);

      SmartDashboard.putNumber("Targets Detected", pipeline.filterContoursOutput().size());

      if (!pipeline.filterContoursOutput().isEmpty()) {

        //Get biggest contour and find its centre

        Rect biggest = new Rect(); // Initialized to 0 area rectangle

        for (int i = 0; i < pipeline.filterContoursOutput().size(); i++) {
          Rect contour = Imgproc.boundingRect(pipeline.filterContoursOutput().get(i));

          if (contour.area() > biggest.area()) biggest = contour;

          Imgproc.rectangle(output, contour, new Scalar(0, 255, 0, 255), 1); // Add rectangle to the output
        }
        
        synchronized (imgLock) {
          xCentre = biggest.x + (biggest.width / 2); //Set the centre of the bounding rectangle
          width = biggest.width;
          SmartDashboard.putNumber("Width", width);
        }
      }
      vOut.putFrame(output);
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
}
