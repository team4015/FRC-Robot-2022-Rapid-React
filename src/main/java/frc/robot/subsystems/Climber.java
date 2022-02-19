 /* ==================================================
 * Authors: Jason and Alexandra
 *
 * --------------------------------------------------
 * Description: 
 * Contains the functions to control the climber.
 *
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Climber extends SubsystemBase
{
  // HARDWARE //
  private Spark spoolmotor;
  private Spark gearmotor;

  // Ports //
  public static final int CLIMBER_SPARK_SPOOL = -1;
  public static final int CLIMBER_SPARK_GEAR = -1;

  // CONSTANTS //

  public static final double SPOOL_SPEED = 0.1;
  public static final double SPOOL_REVERSE_SPEED = -0.1;
  public static final double GEAR_SPEED = 0.1;
  public static final double GEAR_REVERSE_SPEED = -0.1;

  public Climber()
  {
    // instantiate hardware
    spoolmotor = new Spark(CLIMBER_SPARK_SPOOL);
    //the climber is run by a spool and gear motor, controlled by two Spark motor controllers.
    gearmotor = new Spark(CLIMBER_SPARK_GEAR);
  }

  // METHODS //
  //unwind() winds out cabel and strap to climb
public void unwind(){
    spoolmotor.set(SPOOL_REVERSE_SPEED);
  }
  //extend() unwinds cabel, and extends the extension rod
public void extend(){
  unwind();
  gearmotor.set(GEAR_SPEED);
}
//retract() retracts the rod, after the cabel has been unwinded.
public void retract(){
  spoolmotor.set(0);
  gearmotor.set(GEAR_REVERSE_SPEED);
}
//climb() after the rod has been retracted, the spool winds in the cabel, so the robot can climb.
public void climb(){
  spoolmotor.set(SPOOL_SPEED);
  gearmotor.set(0);
}//stop() allows both motors to immedediatly stop spinning 
public void stop(){
  gearmotor.set(0);
  spoolmotor.set(0);
}

}