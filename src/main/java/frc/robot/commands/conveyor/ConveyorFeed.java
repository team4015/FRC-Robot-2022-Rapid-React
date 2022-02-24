/* ==================================================
 * Authors: Fawaaz Kamali Siddiqui, Lucas Jacobs
 *
 * --------------------------------------------------
 * Description: This class contains code to feed balls
 * into the conveyor. 
 * ================================================== */

package frc.robot.commands.conveyor;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ConveyorFeed extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public ConveyorFeed(Robot robot)
  {
    this.robot = robot;

    // subsystems that this command requires
    addRequirements(robot.conveyor);
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
    robot.conveyor.feed();
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
