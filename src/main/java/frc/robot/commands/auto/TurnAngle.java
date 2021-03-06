/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot turn a given angle in degrees using proportional speed
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurnAngle extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private double speed;
  private double degrees;
  private double targetAngle;
  private boolean endCommand;

  // CONSTANTS //
  private static final double MIN_TURN_SPEED = .5
  ;
  private static final double ANGLE_THRESHOLD = 20;

  // CONSTRUCTOR //

  public TurnAngle(Robot robot, double speed, double degrees)
  {
    this.robot = robot;
    this.speed = speed;
    this.degrees = degrees;

    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    targetAngle = robot.drivetrain.gyroAngle() + degrees;
    endCommand = false;

    SmartDashboard.putString("Robot Mode:", "Turn Angle");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    double currentAngle = robot.drivetrain.gyroAngle();
    double error = targetAngle - currentAngle;

    if (Math.abs(error) < ANGLE_THRESHOLD) endCommand =true;
      double turnSpeed = Math.copySign(Math.max(MIN_TURN_SPEED, Math.abs(speed*error)), error); 

      robot.drivetrain.moveMotors(0, turnSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.drivetrain.stopMotors();
    if (SmartDashboard.getString("Robot Mode:", "").equals("Turn Angle")) {
      SmartDashboard.putString("Robot Mode:", "TeleOp");
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished()
  {
    return endCommand;
  }
}
