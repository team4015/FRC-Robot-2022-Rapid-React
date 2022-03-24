/* ==================================================
 * Authors: Lemi Miyu, Fawaaz, and Lucas Jacobs
 *
 * --------------------------------------------------
 * Description: Drivetrain.java contains functions
 * and objects needed with the drivetrain's motors
 * in order to control robot movement.
 * 
 * Also contains methods for controlling the gyroscope and accelerometer.
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.ADXL362;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends SubsystemBase
{
  // HARDWARE //

  // declare motor controllers, solenoids, and other hardware here

  private Talon rightMotor;
  private Talon leftMotor;
  private DifferentialDrive drive;

  private ADXRS450_Gyro gyro;
  private BuiltInAccelerometer accel;

  // VARIABLES //

  private boolean goingStraight;
  private double straightDirection;

  private LinearFilter xAccelFilter;
  private LinearFilter gyroFilter;
  private double angle;
  private double previousXAccel;
  private double previousVelocity;
  private double totalDistance;
  private double posX;
  private double posY;

  // PORTS //

  public static final int RIGHT_MOTOR = 0;
  public static final int LEFT_MOTOR = 1;

  // CONSTANTS //
  public static final double AIM_TURN_SPEED = 0.4;
  public static final double GYRO_CORRECT_SPEED = 0.05;

  //  ****ALL OF THESE CONSTANTS NEED TO BE TUNED BY TESTING****
  private final static int SAMPLES = 10; // Num of samples of accelration in the moving average
  private final static double ACCEL_DEADZONE = 0.3; // Minimum accel for it to count in the program
  private final static double VELOCITY_DEADZONE = .2; // Minimum velocity for it to count in the program
  private final static double STARTING_HUB_DIST = 1; // in metres
  private final static double ACCEL_TO_CENTRE_DIST = 0.3175; //Distance from accelerometer to centre of rotation of robot
  private final static double ACCEL_ANGLE = 43.3; //Angle between the accelerometer and straight backwards on the robot in degrees
  private final static double COS_OF_ACCEL_ANGLE = Math.cos(Math.toRadians(ACCEL_ANGLE));
  private final static double ROTATION_ADJUST_FACTOR = 1.2;
  

  // CONSTRUCTOR //

  public Drivetrain()
  {
    // instantiate hardware
    rightMotor = new Talon (RIGHT_MOTOR);
    leftMotor = new Talon(LEFT_MOTOR);
    leftMotor.setInverted(true);

    drive = new DifferentialDrive(rightMotor, leftMotor);

    gyro = new ADXRS450_Gyro();
    calibrateGyro();

    accel = new BuiltInAccelerometer(); // Measure in the range -4g to +4g
    resetAccelerometer();

    goingStraight = false;
    straightDirection = 0;
  }

  // METHODS // 
  
  // -----------Motor Methods --------

  public void moveMotors (double speed, double turn) {

    double error = 0;
    double currentAngle = gyroAngle();

    // Adjust to make the robot point straight as long as there is no value from the turn joystick
    /*if (goingStraight && speed != 0) { // Adjust the robot if it's moving forward
      if (turn == 0) {
        error = (currentAngle - straightDirection) * GYRO_CORRECT_SPEED;
      } else {
        goingStraight = false;
      }
    } else if (turn == 0 && speed != 0) { // Turn on the turn adjustment if the robot moving without turning
      goingStraight = true;
      straightDirection = currentAngle;
    } else { // The robot isn't going straight if neither of the above run
      goingStraight = false;
    }*/

    drive.arcadeDrive(speed, turn + error);
  }

  public void stopMotors() {
    drive.stopMotor();
  }

  // ----------- Gyro/Accelerometer Methods --------

  public void calibrateGyro() {
    resetGyro();
    gyro.calibrate();
  }

  public void resetGyro() {
    gyro.reset();
    gyroFilter = LinearFilter.movingAverage(SAMPLES);
  }

  public double gyroAngle() {
    return angle;
  }

  public double getTotalDistance() {
    return totalDistance;
  }

  public void resetAccelerometer() {
    xAccelFilter = LinearFilter.movingAverage(SAMPLES);
    previousXAccel = 0;
    previousVelocity = 0;
    totalDistance = 0;
    posX = 0;
    posY = 0;
  }

  @Override
  public void periodic() {
    //Update gyroscope
    angle = gyroFilter.calculate(gyro.getAngle());
    double radians = Math.toRadians(angle);

    //--- Update Robot Position based on the Accelerator ---
    double xAccel = xAccelFilter.calculate(-accel.getX());
    xAccel *= 9.8; // convert accel from g's into m/s^2

    // Compensate for turning acceleration
    double rate = gyro.getRate();
    rate = Math.toRadians(rate);
    // Formula for figuring out the extra acceleration on the accelerometer when the robot is turning (thank you physics class)
    double turningAcceleration = rate*rate*ACCEL_TO_CENTRE_DIST*COS_OF_ACCEL_ANGLE*ROTATION_ADJUST_FACTOR;
    xAccel += turningAcceleration;
    SmartDashboard.putNumber("Acceleration", xAccel);

    //Find avg acceleration
    double averageAccel = (xAccel + previousXAccel)/2;
    previousXAccel = xAccel; // set the previous accel to the the current accel for next loop
    if (Math.abs(averageAccel) < ACCEL_DEADZONE) averageAccel = 0; 

    double velocityChange = averageAccel * .02; // loop time is .02s
    
    double velocity = previousVelocity + velocityChange;
    SmartDashboard.putNumber("Velocity", velocity);

    double averageVelocity = (previousVelocity + velocity)/2;
    previousVelocity = velocity; // set the previous velocity to the the current velocity for next loop
    if (Math.abs(averageVelocity) < VELOCITY_DEADZONE) averageVelocity = 0;

    double distanceChange = averageVelocity * 0.02;

    // If the distance is more than just noise/drift, update the robot's position
    totalDistance += Math.abs(distanceChange);
    posX += distanceChange*Math.sin(radians);
    posY += distanceChange*Math.cos(radians);

    //Update the Dash
    SmartDashboard.putNumber("Gyro Angle", angle);
    SmartDashboard.putNumber("Rotational Acceleration", turningAcceleration);
    SmartDashboard.putNumber("Total Distance", totalDistance);
    SmartDashboard.putNumber("Dist from Hub", Math.sqrt(posX*posX + (posY-STARTING_HUB_DIST)*(posY-STARTING_HUB_DIST)));
    SmartDashboard.putNumber("Dist from Start", Math.sqrt(posX*posX + posY*posY));
    SmartDashboard.putNumber("X Coord", posX);
    SmartDashboard.putNumber("Y Coord", posY);
  }
}
