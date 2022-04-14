/* ==================================================
 * Authors:
 * Kyle Pinto
 * --------------------------------------------------
 * Description:
 * Default command for lights to use colour selected
 * on SmartDashboard
 * ================================================== */

package frc.robot.commands.underglow;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class UnderglowMenu extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public UnderglowMenu(Robot robot)
  {
    this.robot = robot;
    addRequirements(robot.underglow);
  }

  // METHODS //

  @Override
  public void initialize()
  {
    robot.underglow.off();
  }

  @Override
  public void execute()
  {
      robot.underglow.pickColour();
  }

  @Override
  public void end(boolean interrupted)
  {
    robot.underglow.off();
  }

  @Override
  public boolean isFinished()
  {
    return false;
  }
}
