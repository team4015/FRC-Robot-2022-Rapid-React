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
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.ADXL362;
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
  private ADXL362 accel;

  // VARIABLES //

  private boolean goingStraight;
  private double straightDirection;

  private LinearFilter xAccelFilter;
  private double previousXAccel;
  private double previousVelocity;
  private double totalDistance;
  private double posX;
  private double posY;

  // PORTS //

  public static final int RIGHT_MOTOR = 1;
  public static final int LEFT_MOTOR = 0;

  // CONSTANTS //
  public static final double AIM_TURN_SPEED = 0.4;
  public static final double GYRO_CORRECT_SPEED = 0.05;

  //  ****ALL OF THESE CONSTANTS NEED TO BE TUNED BY TESTING****
  private final static int ACCEL_SAMPLES = 5; // Num of samples of accelration in the moving average
  private final static double ACCEL_DEADZONE = 0.0000001; // Minimum accel for it to count in the program
  private final static double VELOCITY_DEADZONE = 0.0000001; // Minimum velocity for it to count in the program
  private final static double STARTING_HUB_DIST = 1; // in metres
  private final static double ACCEL_TO_CENTRE_DIST = 10000; //Distance from accelerometer to centre of rotation of robot
  private final static double ACCEL_ANGLE = 10000; //Angle between the accelerometer and straight backwards on the robot in degrees
  private final static double COS_OF_ACCEL_ANGLE = Math.cos(Math.toRadians(ACCEL_ANGLE));
  

  // CONSTRUCTOR //

  public Drivetrain()
  {
    // instantiate hardware
    rightMotor = new Talon (RIGHT_MOTOR);
    leftMotor = new Talon(LEFT_MOTOR);

    drive = new DifferentialDrive(leftMotor, rightMotor);

    gyro = new ADXRS450_Gyro();
    calibrateGyro();

    accel = new ADXL362(Accelerometer.Range.k4G); // Measure in the range -4g to +4g
    resetAccelerometer();

    goingStraight = false;
    straightDirection = 0;
  }

  // METHODS // 
  
  // -----------Motor Methods --------

  public void moveMotors (double speed, double turn) {

    double adjustedTurn = turn;
    double currentAngle = gyroAngle();

    // Adjust to make the robot point straight as long as there is no value from the turn joystick
    if (goingStraight && speed != 0) {
      if (turn == 0) {
        adjustedTurn = turn - (currentAngle - straightDirection) * GYRO_CORRECT_SPEED;
      } else {
        goingStraight = false;
      }
    } else if (turn == 0 && speed != 0) {
      goingStraight = true;
      straightDirection = currentAngle;
    }

    drive.arcadeDrive(speed, adjustedTurn);
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
  }

  public double gyroAngle() {
    return gyro.getAngle();
  }

  public double getTotalDistance() {
    return totalDistance;
  }

  public void resetAccelerometer() {
    xAccelFilter = LinearFilter.movingAverage(ACCEL_SAMPLES);
    previousXAccel = 0;
    previousVelocity = 0;
    totalDistance = 0;
    posX = 0;
    posY = 0;
  }

  @Override
  public void periodic() {
    //Update gyroscope
    double angle = gyro.getAngle();
    double radians = Math.toRadians(angle);

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
    if (Math.abs(xAccel) < ACCEL_DEADZONE) xAccel = 0;
    double averageAccel = (xAccel + previousXAccel)/2;
    previousXAccel = xAccel; // set the previous accel to the the current accel for next loop

    double velocityChange = averageAccel * .02; // loop time is .02s
    
    double velocity = previousVelocity + velocityChange;
    if (Math.abs(velocity) < VELOCITY_DEADZONE) velocity = 0;
    double averageVelocity = (previousVelocity + velocity)/2;
    previousVelocity = velocity; // set the previous velocity to the the current velocity for next loop

    double distanceChange = averageVelocity * 0.02;

    // If the distance is more than just noise/drift, update the robot's position
    totalDistance += Math.abs(distanceChange);
    posX += distanceChange*Math.sin(radians);
    posY += distanceChange*Math.cos(radians);

    //Update the Dash
    SmartDashboard.putNumber("Gyro Angle", angle);
    SmartDashboard.putNumber("Acceleration", xAccel);
    SmartDashboard.putNumber("Rotational Acceleration", turningAcceleration);
    SmartDashboard.putNumber("Velocity", velocity);
    SmartDashboard.putNumber("Total Distance", totalDistance);
    SmartDashboard.putNumber("Dist from Hub", Math.sqrt(posX*posX + (posY-STARTING_HUB_DIST)*(posY-STARTING_HUB_DIST)));
    SmartDashboard.putNumber("Dist from Start", Math.sqrt(posX*posX + posY*posY));
    SmartDashboard.putNumber("X Coord", posX);
    SmartDashboard.putNumber("Y Coord", posY);
  }
}
