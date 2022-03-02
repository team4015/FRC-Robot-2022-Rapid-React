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

public class Drivetrain extends SubsystemBase
{
  // HARDWARE //

  // declare motor controllers, solenoids, and other hardware here

  private Talon rightMotor;
  private Talon leftMotor;
  private DifferentialDrive drive;

  // PORTS //

  public static final int RIGHT_MOTOR = 1;
  public static final int LEFT_MOTOR = 0;

  // CONSTANTS //
  

  // CONSTRUCTOR //

  public Drivetrain()
  {
    // instantiate hardware
    rightMotor = new Talon (RIGHT_MOTOR);
    leftMotor = new Talon(LEFT_MOTOR);

    drive = new DifferentialDrive(leftMotor, rightMotor);
  }

  // METHODS // 
  
  public void moveMotors (double speed, double turn) {
    drive.arcadeDrive(speed, turn);
  }

  public void stopMotors() {
    drive.stopMotor();
  }
}
