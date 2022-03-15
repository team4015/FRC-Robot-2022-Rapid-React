/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description: Resets the robot's accelerometer
 * ================================================== */

package frc.robot.commands.sensors;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ResetAccel extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public ResetAccel(Robot robot)
  {
    this.robot = robot;

    // subsystems that this command requires
    addRequirements(robot.sensors);
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
    robot.sensors.resetAccelerometer();
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
