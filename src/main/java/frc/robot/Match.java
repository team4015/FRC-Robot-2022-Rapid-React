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

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auto.startmatch.BackUpAndShoot;

public class Match extends TimedRobot
{
  private ADXRS450_Gyro gyro;
  private ADXL362 accel;
  private LinearFilter xAccelFilter;
  private double previousXAccel;
  private double previousVelocity;
  private double totalDistance;
  private double posX;
  private double posY;
  
  private Robot robot;
  private SendableChooser<CommandBase> autoMode;
  private CommandBase auto; 

  //  ****ALL OF THESE CONSTANTS NEED TO BE TUNED BY TESTING****
  private final static int ACCEL_SAMPLES = 5; // Num of samples of accelration in the moving average
  private final static double ACCEL_DEADZONE = 0.0000001; // Minimum accel for it to count in the program
  private final static double VELOCITY_DEADZONE = 0.0000001; // Minimum velocity for it to count in the program
  private final static double STARTING_HUB_DIST = 1; // in metres
  private final static double ACCEL_TO_CENTRE_DIST = 10000; //Distance from accelerometer to centre of rotation of robot
  private final static double ACCEL_ANGLE = 10000; //Angle between the accelerometer and straight backwards on the robot in degrees
  private final static double COS_OF_ACCEL_ANGLE = Math.cos(Math.toRadians(ACCEL_ANGLE));

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit()
  {
    // instantiate the sensors
    gyro = new ADXRS450_Gyro();
    gyro.reset();
    gyro.calibrate();

    accel = new ADXL362(Accelerometer.Range.k8G); // Measure in the range -8g to +8g
    xAccelFilter = LinearFilter.movingAverage(ACCEL_SAMPLES);
    previousXAccel = 0;
    previousVelocity = 0;
    totalDistance = 0;
    posX = 0;
    posY = 0;

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
    //Update gyroscope
    double angle = gyro.getAngle();
    double radians = Math.toRadians(angle);
    SmartDashboard.putNumber("Gyro Angle", angle);

    //--- Update Robot Position based on the Accelerator ---
    double xAccel = xAccelFilter.calculate(accel.getX());
    xAccel *= 9.8; // convert accel from g's into m/s^2

    // Compensate for turning acceleration
    double rate = gyro.getRate();
    rate = Math.toRadians(rate);
    // Formula for figuring out the extra acceleration on the accelerometer when the robot is turning (thank you physics class)
    double turningAcceleration = rate*rate*ACCEL_TO_CENTRE_DIST*COS_OF_ACCEL_ANGLE;
    xAccel += turningAcceleration;

    //Find avg acceleration
    if (xAccel < ACCEL_DEADZONE) xAccel = 0;
    double averageAccel = (xAccel + previousXAccel)/2;
    previousXAccel = xAccel; // set the previous accel to the the current accel for next loop

    double velocityChange = averageAccel * .02; // loop time is .02s
    
    double velocity = previousVelocity + velocityChange;
    if (velocity < VELOCITY_DEADZONE) velocity = 0;
    double averageVelocity = (previousVelocity + velocity)/2;
    previousVelocity = velocity; // set the previous velocity to the the current velocity for next loop

    double distanceChange = averageVelocity * 0.02;

    // If the distance is more than just noise/drift, update the robot's position
    totalDistance += Math.abs(distanceChange);
    posX += distanceChange*Math.sin(radians);
    posY += distanceChange*Math.cos(radians);

    //Update the Dash
    SmartDashboard.putNumber("Acceleration", xAccel);
    SmartDashboard.putNumber("Rotational Acceleration", turningAcceleration);
    SmartDashboard.putNumber("Velocity", velocity);
    SmartDashboard.putNumber("Total Distance", totalDistance);
    SmartDashboard.putNumber("Dist from Hub", Math.sqrt(posX*posX + (posY-STARTING_HUB_DIST)*(posY-STARTING_HUB_DIST)));
    SmartDashboard.putNumber("Dist from Start", Math.sqrt(posX*posX + posY*posY));

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
