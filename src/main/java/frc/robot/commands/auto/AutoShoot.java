/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot shoot a ball at the target
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoShoot extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private Timer timer;
  private double speed;
  private double timeToShot;
  boolean finished;
  private final static double CONVEYOR_SPIN_TIME = 1.5;

  // CONSTANTS //
  final static double TURN_SPEED = 0.1; //CHANGE THIS ALONG WITH VISION PACKAGE

  // CONSTRUCTOR //

  public AutoShoot(Robot robot, double timeToShot, double speed)
  {
    this.robot = robot;
    this.speed = speed;
    timer = new Timer();
    this.timeToShot = timeToShot;
    finished = false;

    // subsystems that this command requires
    addRequirements(robot.drivetrain, robot.shooter, robot.conveyor);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    timer.start();
    SmartDashboard.putString("Robot Mode:", "Auto Shoot");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {

    //Spin shoot
    double autoSpeed = robot.vision.autoShooterSpeed();
    //robot.shooter.spin(autoSpeed);
    robot.shooter.spin(speed);

    //Aim or Shoot
    if (timer.get() < timeToShot) {
      // turn = robot.vision.aimAtTarget(); //Get the turn speed from the camera

      //robot.drivetrain.moveMotors(0, turn * TURN_SPEED);
    } else if (timer.get() < timeToShot + CONVEYOR_SPIN_TIME) {

      robot.conveyor.feed();
    } else {
      finished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.shooter.stop();
    robot.conveyor.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return finished;
  }
}
