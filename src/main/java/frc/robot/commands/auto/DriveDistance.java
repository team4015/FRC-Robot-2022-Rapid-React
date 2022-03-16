/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot drive a certain positive distance in metres using proportional speed
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDistance extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private double speed;
  private double distance;
  private double targetDistance;

  // CONSTANTS //
  private static final double MIN_DRIVE_SPEED = .3;

  // CONSTRUCTOR //

  public DriveDistance(Robot robot, double speed, double distance)
  {
    this.robot = robot;
    this.speed = speed;
    this.distance = distance;

    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    targetDistance = robot.drivetrain.getTotalDistance() + distance;

    SmartDashboard.putString("Robot Mode:", "Turn Angle");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    double currentDistance = Math.abs(robot.drivetrain.getTotalDistance());
    double error = targetDistance - currentDistance;

    while (error > 0) {
      double driveSpeed = Math.max(MIN_DRIVE_SPEED, speed*error); 

      robot.drivetrain.moveMotors(driveSpeed, 0);

      currentDistance = Math.abs(robot.drivetrain.getTotalDistance());
      error = targetDistance - currentDistance;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.drivetrain.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return true;
  }
}
