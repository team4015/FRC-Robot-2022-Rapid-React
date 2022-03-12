/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot turn towards the target if it is in view
 * ================================================== */

package frc.robot.commands.vision;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAim extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //
  static final double TURN_SPEED = 0.4;

  // CONSTRUCTOR //

  public VisionAim(Robot robot)
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
   robot.vision.enableAimingLight();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    double turn = robot.vision.aimAtTarget(); //Get the turn speed from the camera
  
    if (turn > 0) {
      robot.drivetrain.moveMotors(0, Drivetrain.AIM_TURN_SPEED);
    } else if (turn < 0) {
      robot.drivetrain.moveMotors(0, -Drivetrain.AIM_TURN_SPEED);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.vision.disableAimingLight();
  }
  public boolean isFinished()
  {
    return false;
  }
}
