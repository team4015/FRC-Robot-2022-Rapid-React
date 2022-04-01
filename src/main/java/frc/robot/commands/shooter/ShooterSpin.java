/* ==================================================
Authors: Jason Wang 
Description: ShooterSpin.java is a command that 
tells the shooter subsystem to spin the shooting motor
to spin at a certain speed to shoot the ball.
 * ================================================== */
package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ShooterSpin extends CommandBase{
    
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private Robot robot;
  private double speed;

  public ShooterSpin(Robot robot, double speed)
  {
    this.robot = robot;
    this.speed = speed;
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
    robot.shooter.spin(speed);
      
    //change to robot.operator.axisy or smth, depending on what axis it is
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
