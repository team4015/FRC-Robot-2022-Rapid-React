/* ==================================================
 * Authors: Lemi Miyu and Fawaaz
 *
 * --------------------------------------------------
 * Description: Drivetrain.java contains functions
 * and objects needed with the drivetrain's motors
 * in order to control robot movement
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends SubsystemBase
{
  // HARDWARE //

  // declare motor controllers, solenoids, and other hardware here

  private Talon rightMotor;
  private Talon leftMotor;
  private DifferentialDrive drive;

  private boolean goingStraight;
  private double straightDirection;

  // PORTS //

  public static final int RIGHT_MOTOR = 1;
  public static final int LEFT_MOTOR = 0;

  // CONSTANTS //
  public static final double AIM_TURN_SPEED = 0.4;
  public static final double GYRO_CORRECT_SPEED = 0.05;
  

  // CONSTRUCTOR //

  public Drivetrain()
  {
    // instantiate hardware
    rightMotor = new Talon (RIGHT_MOTOR);
    leftMotor = new Talon(LEFT_MOTOR);

    drive = new DifferentialDrive(leftMotor, rightMotor);

    goingStraight = false;
    straightDirection = 0;
  }

  // METHODS // 
  
  public void moveMotors (double speed, double turn) {

    double adjustedTurn = turn;
    double currentAngle = SmartDashboard.getNumber("Gyro Angle", 0);

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
}
