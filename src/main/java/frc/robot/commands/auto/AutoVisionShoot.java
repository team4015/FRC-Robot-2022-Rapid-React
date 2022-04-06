/* ==================================================
 * Authors: Lucas Jacobs
 *
 * --------------------------------------------------
 * Description:
 * Makes the robot aim at the target and shoot based on the speed from the vision
 * ================================================== */

package frc.robot.commands.auto;

import frc.robot.Robot;
import frc.robot.subsystems.Vision;

import java.util.LinkedList;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoVisionShoot extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;
  private Vision vision;
  private Timer timer;
  private LinkedList<Double> speeds;
  private double averageSpeed;
  private boolean endCommand;
  private boolean constantSpeed;
  private double timerInit;
  //private final static double CONVEYOR_SPIN_TIME = .6;

  // CONSTANTS //
  private final static double CONVEYOR_REVERSE_TIME = 0.06;
  private final static int SAVED_SPEEDS = 50;
  private final static double DIFF_THRESHOLD = 0.05; 
  private final static double CONVEYOR_FEED_TIME = 0.5; 
  private final static double TIME_BETWEEN_BALLS = 1;

  // CONSTRUCTOR //

  public AutoVisionShoot(Robot robot)
  {
    this.robot = robot;
    vision = robot.vision;
    timer = new Timer();
    speeds = new LinkedList<Double>();
    averageSpeed = 0;
    endCommand = false;
    constantSpeed = false;
    timerInit = 0;

    // subsystems that this command requires
    addRequirements(robot.shooter, robot.conveyor);
  }

  // METHODS //

  // Called when the command is initially scheduled.
  @Override
  public void initialize()
  {
    vision.enableShootingLight();
    robot.shooter.setAutoShooting(true);
    robot.vision.resetPID();

    timer.start();
    timer.reset();

    SmartDashboard.putString("Robot Mode:", "Auto Shoot");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute()
  {

    if (timer.get() < CONVEYOR_REVERSE_TIME) { // No premature shoots
      robot.conveyor.reverse();
    }

    if (constantSpeed && !constantShooterSpeed()) {
      constantSpeed = false;
      endCommand = true;
    }

    if (constantShooterSpeed() && !constantSpeed) {
      constantSpeed = true;
      timerInit = timer.get();
    } // Wait until the shooter speed is consistent

    if (constantSpeed) {
      if (timer.get() - timerInit < CONVEYOR_FEED_TIME) { // Feed the conveyor while the shooter speed is still consistent
          robot.conveyor.feed();
      } else if (timer.get() - timerInit > CONVEYOR_FEED_TIME + TIME_BETWEEN_BALLS) {
        endCommand = true;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted)
  {
    vision.disableShootingLight();

    robot.shooter.stop();
    robot.conveyor.stop();

    robot.shooter.setAutoShooting(false);

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

  /* ==============================
  * Author: Lucas J
  * 
  * Desc: checks if the shooter has been spinning at a 
  * consistent speed and is aligned to the target.
  * ===============================*/
  private boolean constantShooterSpeed() {
    boolean aligned = robot.vision.isAligned();
    boolean isConsistent = false;
    double currentSpeed = vision.getShooterSpeed();
    speeds.add(currentSpeed);
    averageSpeed += currentSpeed/SAVED_SPEEDS;

    if (speeds.size() > SAVED_SPEEDS) {
      double oldSpeed = speeds.pop();
      averageSpeed -= oldSpeed/SAVED_SPEEDS;

      double oldDiff = Math.abs(oldSpeed - currentSpeed);
      double avgDiff = Math.abs(averageSpeed - currentSpeed);

      isConsistent = oldDiff < DIFF_THRESHOLD && avgDiff < DIFF_THRESHOLD;
    }

    return aligned && isConsistent;
  }
}
