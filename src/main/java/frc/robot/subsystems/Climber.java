 /* ==================================================
 * Authors: Jason and Alexandra
 *
 * --------------------------------------------------
 * Description:
 * Climber.java contains the functions to control the climber.
 *
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends SubsystemBase
{
  // HARDWARE //
  private PWMSparkMax rightSpoolMotor;
  private PWMSparkMax leftSpoolMotor;
  private PWMVictorSPX gearmotor;

  // Ports //
  public static final int CLIMBER_SPARK_SPOOL_RIGHT = 5;
  public static final int CLIMBER_SPARK_SPOOL_LEFT = 7;
  public static final int CLIMBER_SPARK_GEARS = 6;

  // CONSTANTS //

  double SPOOL_SPEED = -.5;
  public static final double SPOOL_REVERSE_SPEED = .3 ;
  public static final double GEAR_SPEED = -1;
  public static final double GEAR_REVERSE_SPEED = 1;

  public Climber()
  {
    // instantiate hardware
    rightSpoolMotor = new PWMSparkMax(CLIMBER_SPARK_SPOOL_RIGHT);
    leftSpoolMotor = new PWMSparkMax(CLIMBER_SPARK_SPOOL_LEFT);
    //the climber is run by spool motors (Spark MAX) and gear motor (Victor SP).
    gearmotor = new PWMVictorSPX(CLIMBER_SPARK_GEARS);

    SmartDashboard.putNumber("Spool Speed", SPOOL_SPEED);
  }

  // METHODS //
  //unwind() winds out cable and strap to climb
public void unwind(){
  rightSpoolMotor.set(SPOOL_REVERSE_SPEED);
    leftSpoolMotor.set(SPOOL_REVERSE_SPEED);
  }
  //extend() unwinds cable, and extends the extension rod
public void extend(){
  gearmotor.set(GEAR_SPEED);
}
//retract() retracts the rod, after the cable has been unwinded.
public void retract(){
  rightSpoolMotor.set(0);
  leftSpoolMotor.set(0);
  gearmotor.set(GEAR_REVERSE_SPEED);
}
//climb() after the rod has been retracted, the spool winds in the cable, so the robot can climb.
public void climb(){
  SPOOL_SPEED = SmartDashboard.getNumber("Spool Speed", SPOOL_SPEED);
  rightSpoolMotor.set(-SPOOL_SPEED);
  leftSpoolMotor.set(SPOOL_SPEED);
  gearmotor.set(0);
}//stop() allows both motors to immedediatly stop spinning
public void stop(){
  gearmotor.set(0);
  rightSpoolMotor.set(0);
  leftSpoolMotor.set(0);
}

}