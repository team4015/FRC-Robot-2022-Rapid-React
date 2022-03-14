/* ==================================================
 * Authors:
 * FIRST
 * --------------------------------------------------
 * Description:
 * This class simply creates an instance of
 * Robot.java so that the subsystems can be interacted
 * with. It also starts up the scheduler so that commands
 * can be scheduled.
 * ================================================== */

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auto.startmatch.BackUpAndShoot;

public class Match extends TimedRobot
{
  private Robot robot;
  private SendableChooser<CommandBase> autoMode;
  private CommandBase auto; 

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit()
  {
    // instantiate the robot
    robot = new Robot();

    //Put Scheduler on Dashboard
    SmartDashboard.putData(CommandScheduler.getInstance());

    //Create menu for commands that run in autonomous
    autoMode = new SendableChooser<>();
    autoMode.setDefaultOption("Back Up and Shoot", new BackUpAndShoot(robot));
    autoMode.addOption("Do nothing", null);
    SmartDashboard.putData(autoMode);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic()
  {
    // run the command scheduler
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit()
  {

  }

  @Override
  public void disabledPeriodic()
  {

  }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit()
  {
    //Start Autonomous Commands
    auto = autoMode.getSelected();

    if (auto != null) {
      auto.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic()
  {

  }

  @Override
  public void teleopInit()
  {
    if (auto != null) {
      auto.cancel();
    }
    
    SmartDashboard.putString("Robot Mode:", "TeleOp");
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic()
  {

  }

  @Override
  public void testInit()
  {
    // cancel all running commands
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic()
  {

  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit()
  {

  }

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic()
  {

  }
}
