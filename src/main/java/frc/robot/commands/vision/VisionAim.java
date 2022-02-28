/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot turn towards the target if it is in view
 * ================================================== */

package frc.robot.commands.vision;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAim extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public VisionAim(Robot robot)
  {
    this.robot = robot;

    // subsystems that this command requires
    addRequirements(robot.vision, robot.drivetrain);
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
    robot.vision.aimAtTarget();
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
