/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot turn towards the target if it is in view
 * ================================================== */

package frc.robot.commands.vision;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAim extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  // CONSTANTS //

  // CONSTRUCTOR //

  public VisionAim(Robot robot)
  {
    this.robot = robot;

    SmartDashboard.putString("Robot Mode:", "Auto Aim");

    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
   robot.vision.enableAimingLight();
   robot.vision.resetPID();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    if (!robot.shooter.isAutoShooting()) robot.vision.calcAlign(robot.drivetrain.gyroAngle());
    
    //if (!robot.vision.isAligned()) {
      robot.drivetrain.moveMotors(0, robot.vision.getTurnSpeed());
    //}
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.vision.disableAimingLight();

    if (SmartDashboard.getString("Robot Mode:", "").equals("Auto Aim")) {
      SmartDashboard.putString("Robot Mode:", "TeleOp");
    }
  }

  public boolean isFinished()
  {
    return false;
  }
}
