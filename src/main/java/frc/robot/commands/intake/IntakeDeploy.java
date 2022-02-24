/* ==================================================
Authour: Shane Pinto
Description: IntakeDeploy.java is a command that 
tells the intake subsystem to deploy the intake 
piston.
================================================== */

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class IntakeDeploy extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private Robot robot;

  public IntakeDeploy(Robot robot)
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
    robot.intake.deploy();
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