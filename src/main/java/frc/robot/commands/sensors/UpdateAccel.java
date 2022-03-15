/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description: Updates the robot's
 * position using the accelerometer
 * ================================================== */

package frc.robot.commands.sensors;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class UpdateAccel extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public UpdateAccel(Robot robot)
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
    robot.sensors.updateAccelerometer();
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
    return false;
  }
}
