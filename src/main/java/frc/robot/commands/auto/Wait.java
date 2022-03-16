/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot do nothing for a given time
 * ================================================== */

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Wait extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Timer timer;
  private double duration;

  // CONSTANTS //

  // CONSTRUCTOR //

  public Wait(double duration)
  {
    timer = new Timer();
    this.duration = duration;

    // subsystems that this command requires
    addRequirements();
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    timer.start();
    timer.reset();
    SmartDashboard.putString("Robot Mode:", "Wait");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    while (timer.get() < duration) {}
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
