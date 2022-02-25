/* ==================================================
Authors: Jason Wang 
Description: ShooterSetSpeed.java is a command that 
tells the shooter subsystem to set the shooting motor speed to a speed
that allows the shooter to shoot the ball into the target.
 * ================================================== */
package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ShooterSetSpeed extends CommandBase{
    
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private Robot robot;

  public ShooterSetSpeed(Robot robot)
  {
    this.robot = robot;
    addRequirements(robot.shooter);
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
    robot.shooter.setSpeed();
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
