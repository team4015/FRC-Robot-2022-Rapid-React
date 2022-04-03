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
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends SubsystemBase
{
  // HARDWARE //

  // declare motor controllers, solenoids, and other hardware here

  private final Talon rightMotor;
  private final Talon leftMotor;
  private final DifferentialDrive drive;

  private final Encoder leftEncoder;
  private final Encoder rightEncoder;

  private final ADXRS450_Gyro gyro;
  private final BuiltInAccelerometer accel;

  // VARIABLES //

  private boolean goingStraight;
  private double targetAngle;
  private boolean coasting;

  private final DifferentialDriveOdometry odometry;

  // PORTS //

  public static final int RIGHT_MOTOR = 0;
  public static final int LEFT_MOTOR = 1;
  public static final int LEFT_ENCODER_A = 0;
  public static final int LEFT_ENCODER_B = 1;
  public static final int RIGHT_ENCODER_A = 2;
  public static final int RIGHT_ENCODER_B = 3;


  // CONSTANTS //
  public static final double AIM_TURN_SPEED = 0.03;
  public static final double GYRO_CORRECT_SPEED = 0.05;

  //For auto aim
  public static final double MAX_TURN_SPEED = 0.5;
  public static final double MAX_RATE = 10; // test dis
  public static final double ERROR_CORRECT = .07;
  //end auto aim

  private final static double ACCEL_TO_CENTRE_DIST = 0.3175; //Distance from accelerometer to centre of rotation of robot
  private final static double ACCEL_ANGLE = 43.3; //Angle between the accelerometer and straight backwards on the robot in degrees
  private final static double COS_OF_ACCEL_ANGLE = Math.cos(Math.toRadians(ACCEL_ANGLE));
  private final static double ROTATION_ADJUST_FACTOR = 1.2;

  //encoder constants
  private final static double WHEEL_RADIUS = Units.inchesToMeters(6)/2; //In Metres
  private final static double ENCODER_RESOLUTION = 256;
  private final static double GEAR_RATIO = 1/4.67; //Number of turns the wheel makes for each turn of the motor
  private final static double DISTANCE_PER_PULSE = 2*Math.PI*WHEEL_RADIUS*GEAR_RATIO/ENCODER_RESOLUTION;
  

  // CONSTRUCTOR //

  public Drivetrain()
  {
    // instantiate hardware
    rightMotor = new Talon (RIGHT_MOTOR);
    leftMotor = new Talon(LEFT_MOTOR);
    leftMotor.setInverted(true);
    targetAngle = 0;
    goingStraight = false;
    coasting = false;

    drive = new DifferentialDrive(rightMotor, leftMotor);

    gyro = new ADXRS450_Gyro();
    calibrateGyro();
    accel = new BuiltInAccelerometer();

    //Set up Encoders
    leftEncoder = new Encoder(LEFT_ENCODER_A, LEFT_ENCODER_B);
    rightEncoder = new Encoder(RIGHT_ENCODER_A, RIGHT_ENCODER_B);

    leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
    rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);

    resetEncoders();

    odometry = new DifferentialDriveOdometry(gyroRotation2d());
  }

  // METHODS // 
  
  // -----------Motor Methods --------

  public void moveMotors (double speed, double turn) {
    SmartDashboard.putNumber("Rate", gyro.getRate());

    if (turn == 0) { // **** Robot is moving straight

      if (!goingStraight) {
        goingStraight = true;
        coasting = true;
      }

      if (coasting) {
        drive.arcadeDrive(speed, 0);
        if (Math.abs(gyro.getRate()) < MAX_RATE) {
          coasting = false;
          targetAngle = gyroAngle();
          SmartDashboard.putNumber("Target Angle", targetAngle);
        }
      } else {
        double currentAngle = gyroAngle();

        //targetAngle += turn*POWER_TO_DEGREES;

        double error = targetAngle - currentAngle;
        SmartDashboard.putNumber("Error", error);

        double turnSpeed = error*ERROR_CORRECT;
        SmartDashboard.putNumber("Turn Speed", turnSpeed);

        if (Math.abs(turnSpeed) > MAX_TURN_SPEED) turnSpeed = Math.copySign(MAX_TURN_SPEED, turnSpeed); 

        drive.arcadeDrive(speed, turnSpeed);
      }

    } else { // **** Robot is turning
      goingStraight = false;
      drive.arcadeDrive(speed, turn);
    }
  }

  /* =============================
   * Author: Lucas Jacobs
   * 
   * Desc: Directly controls the left and right sides of the
   * drivetrain by setting voltages.
   * =========================================*/
  public void tankDriveVoltage(double leftVolts, double rightVolts) {
    leftMotor.setVoltage(leftVolts);
    rightMotor.setVoltage(rightVolts);
    drive.feed();
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

  public Rotation2d gyroRotation2d() {
    return gyro.getRotation2d();
  }

  //------------ Encoder Methods ---------------
  
  public void resetEncoders() {
    leftEncoder.reset();
    rightEncoder.reset();
  }

  public Pose2d getRobotPose() {
    return odometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    odometry.resetPosition(pose, gyroRotation2d());
  }

  /* =============================
   * Author: Lucas Jacobs
   * 
   * Desc: Returns the speed of the left and right
   * encoders in m/s
   * =========================================*/

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(leftEncoder.getRate(), rightEncoder.getRate());
  }

  @Override
  public void periodic() {
    // --- Update Odometry ---
    Pose2d currentPos = odometry.update(gyroRotation2d(), leftEncoder.getDistance(), rightEncoder.getDistance());

    //--- Update Robot Position based on the Accelerator ---
    double xAccel = -accel.getX();
    xAccel *= 9.8; // convert accel from g's into m/s^2

    // Compensate for turning acceleration
    double rate = gyro.getRate();
    rate = Math.toRadians(rate);
    // Formula for figuring out the extra acceleration on the accelerometer when the robot is turning (thank you physics class)
    double turningAcceleration = rate*rate*ACCEL_TO_CENTRE_DIST*COS_OF_ACCEL_ANGLE*ROTATION_ADJUST_FACTOR;
    xAccel += turningAcceleration;
   
    //Update the Dash

    SmartDashboard.putNumber("Gyro Angle", gyroAngle());
    SmartDashboard.putNumber("Acceleration", xAccel);
    SmartDashboard.putNumber("X Coord", currentPos.getX());
    SmartDashboard.putNumber("Y Coord", currentPos.getY());
  }
}
