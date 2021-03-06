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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auto.startMatch.BackUpAndShoot;
import frc.robot.commands.auto.startMatch.TwoBallAuto;

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

    // Disable Live Window that was causing loop overruns
    LiveWindow.disableAllTelemetry();

    //Create menu for commands that run in autonomous
    autoMode = new SendableChooser<>();
    autoMode.setDefaultOption("(Away) Two Ball Auto", new TwoBallAuto(robot));
    autoMode.addOption("(Toward) One Ball Auto", new BackUpAndShoot(robot));
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

    SmartDashboard.putBoolean("Has Pressure", robot.compressor.getPressureSwitchValue());
    SmartDashboard.putNumber("Time Remaining", Math.ceil(Timer.getMatchTime()));

    if (robot.shooter.isAutoShooting() || robot.drivetrain.isAutoAiming()) robot.vision.calcAlign(robot.drivetrain.gyroAngle());
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
    //Put Scheduler on Dashboard agian in case of reboot
    SmartDashboard.putData(CommandScheduler.getInstance());

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

    robot.operator.disableUnwind();

    //Put Scheduler on Dashboard agian in case of reboot
    SmartDashboard.putData(CommandScheduler.getInstance());
    
    if (auto != null) {
      auto.cancel();
    }
    
    SmartDashboard.putString("Robot Mode:", "TeleOp");
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic()
  {
    double time = Timer.getMatchTime();
    if (time < 1 && time>0) robot.intake.retract();
  }

  @Override
  public void testInit()
  {
    // cancel all running commands
    CommandScheduler.getInstance().cancelAll();

    robot.operator.enableUnwind();
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
