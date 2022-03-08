/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description: Sets the Drivetrain high speed
 * ================================================== */

package frc.robot.commands.driver;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class HighSpeed extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public HighSpeed(Robot robot)
  {
    this.robot = robot;

    // subsystems that this command requires
    // addRequirements(); none needed
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
    robot.driver.useHighSpeed();
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
