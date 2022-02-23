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
import edu.wpi.first.wpilibj.motorcontrol.Talon;

public class Drivetrain extends SubsystemBase
{
  // HARDWARE //

  // declare motor controllers, solenoids, and other hardware here

  private Talon rightMotor;
  private Talon leftMotor;

  // PORTS //

  public static final int RIGHT_MOTOR = -1; // port not final
  public static final int LEFT_MOTOR = -2; // port not final

  // CONSTANTS //

  

  // CONSTRUCTOR //

  public Drivetrain()
  {
    // instantiate hardware
    rightMotor = new Talon (RIGHT_MOTOR);
    leftMotor = new Talon(LEFT_MOTOR);
  }

  // METHODS //

  public void moveMotors (double speed) {
    rightMotor.set(speed);
    leftMotor.set(speed);
  }

  public void stopMotors() {
    rightMotor.set(0);
    leftMotor.set(0);
  }
}
