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

import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
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

  // CONSTANTS //

  // Set using SysID Characterization
  private final static double KS = 1.2374;
  private final static double KV = 5.6065;
  private final static double KA = 2;
  private final static double KP_DRIVE = 3.2641; // Proportionality constant for the drive PID
  private final static double TRACK_WIDTH_METRES = 0.59;
  private final static DifferentialDriveKinematics DRIVE_KINEMATICS = new DifferentialDriveKinematics(TRACK_WIDTH_METRES);
  private final static double MAX_VOLTAGE = 10; // Below 12 to account for battery sag
  private final static double MAX_VELOCITY = 5; // metres per second
  private final static double MAX_ACCELERATION = 20; // metres per second^2
  private final static double RAMSETE_B = 2; //Given value from wpilib
  private final static double RAMSETE_ZETA = 0.7; // Given value from wpilib
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
    intake.retract(); // retract default

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

  /* =============================
   * Author: Lucas Jacobs
   * 
   * Desc: Returns the command that the robot will use in 
   * autonomous mode. The command makes the robot follow a given trajectory.
   * =========================================*/

  public CommandBase getAutonomousCommand() {
    
    // Make feed forward for Motors
    SimpleMotorFeedforward feedForward = new SimpleMotorFeedforward(KS, KV, KA);

    // Make voltage constraint
    DifferentialDriveVoltageConstraint voltageConstraint = new DifferentialDriveVoltageConstraint(
      feedForward,
      DRIVE_KINEMATICS,
      MAX_VOLTAGE
    );

    // Make config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(MAX_VELOCITY, MAX_ACCELERATION)
      // Make sure max speed is obeyed
      .setKinematics(DRIVE_KINEMATICS)
      // Add voltage Constraint
      .addConstraint(voltageConstraint);

    // Example Trajectory (Real Auto Trajectories to come later)
    Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
      // Start Point
      new Pose2d(0, 0, new Rotation2d(0)), 
      // Waypoints
      List.of(new Translation2d(3, 0)),//List.of(new Translation2d(1, 1), new Translation2d(2, -1)), 
      // End Point
      new Pose2d(3, 0, new Rotation2d(0)), 
      // Pass in the config
      config);

    // Reset odometry to starting point of trajectory
    drivetrain.resetOdometry(trajectory.getInitialPose());

    // Make command to be sent out
    RamseteCommand ramseteCommand = new RamseteCommand(
      // Trajectory to be followed
      trajectory, 
      // Method to get robot pose
      drivetrain::getRobotPose, 
      // Controller that does path following computation
      new RamseteController(RAMSETE_B, RAMSETE_ZETA), 
      // Motor feed Forward
      feedForward,
      // Drivetrain Kinematics 
      DRIVE_KINEMATICS, 
      // Method to get wheel speeds
      drivetrain::getWheelSpeeds, 
      // PID Controller for left motor
      new PIDController(KP_DRIVE, 0, 0), 
      // PID controller for right motor
      new PIDController(KP_DRIVE, 0, 0), 
      // Method for setting the voltage on each motor
      drivetrain::tankDriveVoltage, 
      // Pass drivetrain as a requirement
      drivetrain);

      // Run command, then stop
      return ramseteCommand.andThen(() -> drivetrain.tankDriveVoltage(0, 0));
  }
}