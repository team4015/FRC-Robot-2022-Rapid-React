/* ==================================================
 * Authors:
 *
 * --------------------------------------------------
 * Description:
 * This class that contains all the necessary things
 * to make a robot instance. This class will make
 * instances of each subsystem, initialize a startup
 * function for each subsystem, and then set a default
 * command for each subsystem to use. It will also
 * instantiate Driver.java to allow driver input via
 * joysticks to control the robot.
 * ================================================== */

package frc.robot;

import frc.robot.commands.drivetrain.*;
import frc.robot.commands.intake.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.climber.*;
import frc.robot.commands.conveyor.*;
import frc.robot.commands.shooter.*;
import frc.robot.controls.*;
import frc.robot.subsystems.*;

public class Robot
{
  // CONTROLS //
  public Driver driver;
  public Operator operator;

  // SUBSYSTEMS //
  public Compressor compressor;
  public Climber climber;
  public Conveyor conveyor;
  public Drivetrain drivetrain;
  public Intake intake;
  public Shooter shooter;
  public Vision vision;


  // CONSTRUCTOR //

  public Robot()
  {
    // Instantiate Subsystems
    compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    climber = new Climber();
    conveyor = new Conveyor();
    drivetrain = new Drivetrain();
    intake = new Intake();
    shooter = new Shooter();
    vision = new Vision();

    // Instantiate Controls
    driver = new Driver(this);
    operator = new Operator(this);

    initialize();
    setDefaultCommands();
  }

  // METHODS //

  /* -------------------------------------
   * initialize() will tell each
   * subsystem to perform a starting function.
   * Often this is to just stop all motors
   * and set the robot to a starting position.
   * ------------------------------------- */
  private void initialize()
  {
    compressor.enableDigital();

    drivetrain.stopMotors();
    driver.useHighSpeed();
    SmartDashboard.putString("Drive Speed", "HIGH");

    intake.stop();
    intake.deploy(); // Deploy default for humber, (no piston yet)

    climber.stop();
    conveyor.stop();

    vision.initCamera();
    shooter.stop();
  }

  /* -------------------------------------
   * setDefaultCommands() will tell each
   * subsystem which command should be run
   * when no other command is being scheduled.
   * ------------------------------------- */
  private void setDefaultCommands()
  {
    intake.setDefaultCommand(new IntakeStop(this));
    climber.setDefaultCommand(new ClimberStop(this));
    drivetrain.setDefaultCommand(new Drive(this));
    conveyor.setDefaultCommand(new ConveyorStop(this));
    shooter.setDefaultCommand(new ShooterStop(this));
  }
}