/* ==================================================
Authors: Jason Wang and Alexandra Nguyen
Description: ClimberExtend.java is a command that 
tells the climber subsystem to reverse the spin of the spool 
and spin the gear to extend the climbing rod. 
 * ================================================== */
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ClimberExtend extends CommandBase{
    
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private Robot robot;

  public ClimberExtend(Robot robot)
  {
    this.robot = robot;
    addRequirements(robot.climber);
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
    robot.climber.extend();
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
