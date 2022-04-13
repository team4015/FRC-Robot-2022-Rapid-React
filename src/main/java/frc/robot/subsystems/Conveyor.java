/* ==================================================
 * Authors: Fawaaz Kamali Siddiqui, Muhammad 
 *
 * --------------------------------------------------
 * Description: This class contains methods to move balls
 * in the conveyor. 
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Conveyor extends SubsystemBase
{
  // HARDWARE //
  private PWMSparkMax conveyorMotor;

  // declare motor controllers, solenoids, and other hardware here

  // CONSTANTS //
  final static int CONVEYOR_PORT = 3;

  final static double CONVEYOR_SPEED = 0.4;
  final static double SLOW_CONVEYOR_SPEED = 0.2;
  final static double CONVEYOR_REVERSE_SPEED = -0.2;

  // CONSTRUCTOR //

  public Conveyor()
  {
    // instantiate hardware
    conveyorMotor = new PWMSparkMax(CONVEYOR_PORT);
  }

  // METHODS //

  // This method will intake balls into the bay. 
  public void feed() {
    conveyorMotor.set(CONVEYOR_SPEED);
  }

  // This method will intake balls into the bay. 
  public void feedSlow() {
    conveyorMotor.set(SLOW_CONVEYOR_SPEED);
  }

  // This function will reverse the conveyor to outtake balls.
  public void reverse() {
    conveyorMotor.set(CONVEYOR_REVERSE_SPEED);
  }

  // This function stops the conveyor from running.
  public void stop() {
    conveyorMotor.set(0);
  }
}
