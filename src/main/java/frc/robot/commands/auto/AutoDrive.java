/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot drive at the specified speed for the specified time
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoDrive extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private Timer timer;
  private double duration;
  private double speed;
  private double turn;
  boolean finished;

  // CONSTANTS //
  final static double TURN_SPEED = 0.1; //CHANGE THIS ALONG WITH VISION PACKAGE

  // CONSTRUCTOR //

  public AutoDrive(Robot robot, double speed, double turn, double duration)
  {
    this.robot = robot;
    timer = new Timer();
    this.speed = speed;
    this.turn = turn;
    this.duration = duration;
    finished = false;

    // subsystems that this command requires
    addRequirements(robot.drivetrain);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    timer.start();
    SmartDashboard.putString("Robot Mode:", "Auto Drive");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {

    if (timer.get() < duration) {
      robot.drivetrain.moveMotors(speed, turn);
    } else {
      finished = true;
    }
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
    return finished;
  }
}
