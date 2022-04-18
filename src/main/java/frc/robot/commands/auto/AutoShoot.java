/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot shoot a ball at the target after a specified time
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoShoot extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private Timer timer;
  private double speed;
  private double timeToShot;
  private boolean timerReset;
  private boolean endCommand;
  private final static double CONVEYOR_SPIN_TIME = .5;

  // CONSTANTS //

  // CONSTRUCTOR //

  public AutoShoot(Robot robot, double speed, double timeToShot)
  {
    this.robot = robot;
    this.speed = speed;
    this.timeToShot = timeToShot;
    timer = new Timer();
    timerReset = false;

    // subsystems that this command requires
    addRequirements(robot.shooter, robot.conveyor);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    timer.start();
    timer.reset();
    timerReset = false;
    endCommand = false;
    SmartDashboard.putString("Robot Mode:", "Auto Shoot");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {
    //Spin shooter
    if (timer.get() < timeToShot) {
      robot.shooter.spin(speed);
    } else if (timer.get() < CONVEYOR_SPIN_TIME) {

    if (!timerReset) {
      timer.reset();
      timerReset = true;
    }

    //Spin shooter and feed the conveyor
      robot.shooter.spin(speed);
      robot.conveyor.feed();
    } else {
      endCommand = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    robot.shooter.stop();
    robot.conveyor.stop();
    
    if (SmartDashboard.getString("Robot Mode:", "").equals("Auto Shoot")) {
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
