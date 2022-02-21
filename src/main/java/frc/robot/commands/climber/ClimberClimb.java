/* ==================================================
Authors: Jason Wang and Alexandra Nguyen
Description: ClimberClimb.java is a command that 
tells the climber subsystem to spin the spool and stop
the spin of the gear to allow the robot to climb. 
 * ================================================== */
package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ClimberClimb extends CommandBase{
    
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private Robot robot;

  public ClimberClimb(Robot robot)
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
    robot.climber.climb();
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
