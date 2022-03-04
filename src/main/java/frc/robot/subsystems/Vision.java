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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  // HARDWARE //

  // PORTS //

  // CONSTANTS //
  final static int HEIGHT = 480;
  final static int WIDTH = 640;
  final static int FPS = 30;

  final static int TURN_THRESHOLD = 20;

  // VARIABLES //
  private VisionThread visionThread;
  private double xCentre;
  private Object imgLock;


  public Vision() {
    xCentre = WIDTH/2.0;
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
    CvSource vOut = CameraServer.putVideo("VideoOutput", 640, 480);

    cam.setResolution(WIDTH, HEIGHT);
    cam.setFPS(FPS);

    // initalize vision thread
    visionThread = new VisionThread(cam, new GripPipeline(), pipeline -> {
      
      //Create output frames which will have rectangles drawn on them
      Mat output = new Mat();
      vIn.grabFrame(output);

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
    double turn = xCentre - (WIDTH/ 2.0);

    // If the robot is within the turn threshold of pointing straight at the target, it wil stop turning
    if (Math.abs(turn) < TURN_THRESHOLD) turn = 0;

    return turn; // return difference between the target and where the robot is pointed
  }
  public double shooterMotorSpeed(){
    double distance = 6.9;
    double shooterAngle = 69;
    double radiusWheel = 1.143;
    double gravityConstant = 9.8;
    double targetHeight = 2.642;
    double speed = (distance)/((Math.cos(shooterAngle))*(Math.sqrt((2*(-targetHeight+distance*Math.tan(shooterAngle))/gravityConstant))));
    double shooterMotorSpeed= speed/radiusWheel;
    return shooterMotorSpeed;

    
  }
}
