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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAim extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private double previousTurn;
  private double angleError;
  private double currentAngle;

  // CONSTANTS //
  private final static double PIXELS_TO_DEGREES = 0.35;
  private final static double THRESHOLD = 3;
  private final static double MIN_TURN_SPEED = 0.4;
  private final static double MAX_TURN_SPEED = 0.7;


  // CONSTRUCTOR //

  public VisionAim(Robot robot)
  {
    this.robot = robot;
    previousTurn = 100000;
    angleError = 0;
    currentAngle = 0;

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
    if (turn == previousTurn) {
      angleError +=  robot.drivetrain.gyroAngle() - currentAngle; 
      currentAngle = robot.drivetrain.gyroAngle();
    } else {
      previousTurn = turn;
      angleError = turn*PIXELS_TO_DEGREES;
      currentAngle = robot.drivetrain.gyroAngle();
    }

    SmartDashboard.putNumber("Angle Error", angleError);
    double turnSpeed = angleError*Drivetrain.AIM_TURN_SPEED;

    if (Math.abs(turnSpeed) < MIN_TURN_SPEED) turnSpeed = Math.copySign(MIN_TURN_SPEED, turnSpeed);
    if (Math.abs(turnSpeed) > MAX_TURN_SPEED) turnSpeed = Math.copySign(MIN_TURN_SPEED, turnSpeed);
    if (Math.abs(angleError) > THRESHOLD) {
      SmartDashboard.putBoolean("ALIGNED", false);
      robot.drivetrain.moveMotors(0, turnSpeed);
    } else {
      SmartDashboard.putBoolean("ALIGNED", true);
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
