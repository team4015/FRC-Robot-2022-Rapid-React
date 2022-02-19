 /* ==================================================
 * Authors: Jason and Alexandra
 *
 * --------------------------------------------------
 * Description:
 *
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class Climber extends SubsystemBase
{
  private Spark spoolmotor;
  private Spark gearmotor;
  
  // HARDWARE //

  // Ports //
  public static final int CLIMBER_SPARK_SPOOL = -1;
  public static final int CLIMBER_SPARK_GEAR = -1;

  // declare motor controllers, solenoids, and other hardware here

  // CONSTANTS //

  public static final double SPOOL_SPEED = 0.1;
  public static final double SPOOL_REVERSE_SPEED = -0.1;
  public static final double GEAR_SPEED = 0.1;
  public static final double GEAR_REVERSE_SPEED = -0.1;

  // CONSTRUCTOR //

  public Climber()
  {
    // instantiate hardware
    spoolmotor = new Spark(CLIMBER_SPARK_SPOOL);
    gearmotor = new Spark(CLIMBER_SPARK_GEAR);
  }

  // METHODS //
public void extend(){
  spoolmotor.set(SPOOL_REVERSE_SPEED);
  gearmotor.set(GEAR_SPEED);
}
public void unextend(){
  spoolmotor.set(0);
  gearmotor.set(GEAR_REVERSE_SPEED);
}
public void climb(){
  spoolmotor.set(SPOOL_SPEED);
  gearmotor.set(0);
}
public void stop(){
  gearmotor.set(0);
  spoolmotor.set(0);
}

}