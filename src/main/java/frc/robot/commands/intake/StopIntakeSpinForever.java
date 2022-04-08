/* ==================================================
Author: Lucas Jacobs
Description: Stops the intake from spinning forever
================================================== */

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class StopIntakeSpinForever extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private Robot robot;

  public StopIntakeSpinForever(Robot robot)
  {
    this.robot = robot;
    addRequirements(robot.intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    robot.intake.setDefaultCommand(new IntakeStop(robot));
    robot.intake.stop();
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
