/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot drive a given distance
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveDistance extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private double distance;

  // CONSTANTS //
  private static final double DRIVE_SPEED = .7;

  // CONSTRUCTOR //

  public DriveDistance(Robot robot, double distance)
  {
    this.robot = robot;
    this.distance = distance;
    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    robot.drivetrain.resetOdometry(new Pose2d(0,0,new Rotation2d(0)));

    SmartDashboard.putString("Robot Mode:", "Drive Distance");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    while (robot.drivetrain.updateOdometry().getX() < distance) {
      robot.drivetrain.moveMotors(DRIVE_SPEED, 0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.drivetrain.stopMotors();
    if (SmartDashboard.getString("Robot Mode:", "").equals("Drive Distance")) {
      SmartDashboard.putString("Robot Mode:", "TeleOp");
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return true;
  }
}
