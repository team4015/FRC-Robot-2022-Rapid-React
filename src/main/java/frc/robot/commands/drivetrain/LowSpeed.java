/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description: Sets the Drivetrain to low speed
 * ================================================== */

package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LowSpeed extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public LowSpeed(Robot robot)
  {
    this.robot = robot;

    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    robot.driver.MAX_THROTTLE_SPEED = 0.3;
    robot.driver.MAX_STEER_SPEED = 0.2;
    SmartDashboard.putString("Drive Speed", "LOW");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return true;
  }
}
