/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot aim at the target and shoot based on the speed from the vision
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoVisionShoot extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private Timer timer;
  private double timeToShot;
  private final static double CONVEYOR_SPIN_TIME = 1;

  // CONSTANTS //
  final static double TURN_SPEED = 0.2;

  // CONSTRUCTOR //

  public AutoVisionShoot(Robot robot, double timeToShot)
  {
    this.robot = robot;
    this.timeToShot = timeToShot;
    timer = new Timer();

    // subsystems that this command requires
    addRequirements(robot.vision, robot.drivetrain, robot.shooter, robot.conveyor);
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
    SmartDashboard.putBoolean("ALIGNED", false);

    //Aim and spin shooter
    while (timer.get() < timeToShot) {
      double autoSpeed = robot.vision.autoShooterSpeed();
      robot.shooter.spin(autoSpeed);

      double turn = robot.vision.aimAtTarget(); //Get the turn speed from the camera

      if (turn > 0) {
        robot.drivetrain.moveMotors(0, Drivetrain.AIM_TURN_SPEED);
      } else if (turn < 0) {
        robot.drivetrain.moveMotors(0, -Drivetrain.AIM_TURN_SPEED);
      }
    }

    timer.reset();
    
    //Aim, spin and shoot

    while (timer.get() < CONVEYOR_SPIN_TIME) {
      double autoSpeed = robot.vision.autoShooterSpeed();
      robot.shooter.spin(autoSpeed);

      double turn = robot.vision.aimAtTarget(); //Get the turn speed from the camera

      if (turn > 0) {
        robot.drivetrain.moveMotors(0, Drivetrain.AIM_TURN_SPEED);
      } else if (turn < 0) {
        robot.drivetrain.moveMotors(0, -Drivetrain.AIM_TURN_SPEED);
      }

      robot.conveyor.feed();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.shooter.stop();
    robot.conveyor.stop();
    robot.drivetrain.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return true;
  }
}
