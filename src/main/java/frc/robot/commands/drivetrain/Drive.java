/* ==================================================
 * Authors: Lemi Miyu and Fawaaz
 *
 * --------------------------------------------------
 * Description: Command that will send joystick
 * readings to the drivetrain subsystem to write a 
 * speed to the motors so that the robot can move
 * according to joystick input.
 * ================================================== */

package frc.robot.commands.drivetrain;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //



  // CONSTRUCTOR //

  public Drive(Robot robot)
  {
    this.robot = robot;

    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    robot.drivetrain.stopMotors();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    robot.drivetrain.moveMotors(robot.driver.getThrottle(), robot.driver.getSteer());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.drivetrain.stopMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return false;
  }
}
