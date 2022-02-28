/* ==================================================
 * Author: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 *
 * Contains functions needed to control the vision system.
 * ================================================== */

package frc.robot.subsystems;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class Vision extends SubsystemBase {
  // HARDWARE //

  // PORTS //

  // CONSTANTS //
  final static int HEIGHT = 120;
  final static int WIDTH = 160;
  final static int FPS = 30;
  final static double TURN_SPEED = 0.1;

  // VARIABLES //
  private VisionThread visionThread;
  private double xCentre;
  private Object imgLock;
  private Robot robot;


  public Vision(Robot robot) {
    this.robot = robot;
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

    cam.setResolution(WIDTH, HEIGHT);
    cam.setFPS(FPS);

    // initalize vision thread
    visionThread = new VisionThread(cam, new GripPipeline(), pipeline -> {
      
      if (!pipeline.filterContoursOutput().isEmpty()) {
        Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)); //Gets bounding rectangle around the target
        synchronized (imgLock) {
          xCentre = r.x + (r.width / 2); //Set the sentre of the bounding rectangle
        }
      }
    });

    visionThread.start();
  }

  /* =====================================
  Author: Lucas Jacobs

  Desc:
  This method makes the robot turn in place 
  to face the target
  ===================================== */
  public void aimAtTarget() {
    double xCentre;
    synchronized (imgLock) {
        xCentre = this.xCentre;
    }
    double turn = xCentre - (WIDTH / 2); // find difference between the target and where the robot is pointed
    robot.drivetrain.moveMotors(0, turn * TURN_SPEED);
  }
}
