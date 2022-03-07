/* ==================================================
 * Authors: Jason Wang
 *
 * --------------------------------------------------
 * Description: Shooter.java contains the functions to control the shooter.
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;

public class Shooter extends SubsystemBase
{
  // HARDWARE //
  private PWMTalonSRX motor;
 // Ports //
 public static final int SHOOTER_MOTOR = 2;
 // Constants //
 public static final double DEFAULT_SHOOTER_SPEED= 0.5;
  public Shooter()
  {
    // instantiate hardware
    motor = new PWMTalonSRX(SHOOTER_MOTOR);
  }
  // METHODS //
  // spins the motor for the shooter
  public void spin(double speed){
    /*if(speed>1){
      speed=1;
    }else if(speed<0){
      speed=0;
    }*/
    motor.set(speed);
  }//stops the motor for the shooter
  
  public void stop(){
    motor.set(0);
  }//updates the speed for the motor to accomodate the distance from the target
  // this updated speed would be the speed the algorithm calculates in vision.java
}

//things to go in vision:
//constants:
// double angleShooter = 0;
 //double targetDistance = 0;
 //double radiusWheel = 0;
 //public static final double gravityConstant = 0;
 //public static final double targetHeight = 2.642;
//algorithm:
//speed = ((targetDistance)/((Math.cos(angleShooter))*(Math.sqrt((-2*(targetHeight-targetDistance*Math.tan(angleShooter))/gravityConstant)))))/radiusWheel;
//motorspeed= speed/radiuswheel

