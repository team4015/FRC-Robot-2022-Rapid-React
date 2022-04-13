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
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends SubsystemBase
{
  // HARDWARE //
  private VictorSP leftSpoolMotor;
  private VictorSP rightSpoolMotor;
  private VictorSP gearmotor;

  // Ports //
  public static final int CLIMBER_SPOOL = 9;//5
  public static final int CLIMBER_SPOOL_2 = 8;//7
  public static final int CLIMBER_GEAR = 6;

  // CONSTANTS //

  double SPOOL_SPEED = 1;
  public static final double SPOOL_REVERSE_SPEED = .3 ;
  public static final double GEAR_SPEED = 1;
  public static final double GEAR_REVERSE_SPEED = -1;
  private boolean extending;
  public Climber()
  {
    // instantiate hardware
    leftSpoolMotor = new VictorSP(CLIMBER_SPOOL);
    rightSpoolMotor = new VictorSP(CLIMBER_SPOOL_2);
    //the climber is run by a spool and gear motor, controlled by two Spark motor controllers.
    gearmotor = new VictorSP(CLIMBER_GEAR);

    SmartDashboard.putNumber("Spool Speed", SPOOL_SPEED);
    extending = false;
  }

  // METHODS //
  //unwind() winds out cable and strap to climb
public void unwind(){
  leftSpoolMotor.set(SPOOL_REVERSE_SPEED);
    rightSpoolMotor.set(SPOOL_REVERSE_SPEED);
  }
  //extend() unwinds cable, and extends the extension rod
public void extend(){
  gearmotor.set(GEAR_SPEED);
  extending = true;
}
//retract() retracts the rod, after the cable has been unwinded.
public void retract(){
  leftSpoolMotor.set(0);
  rightSpoolMotor.set(0);
  gearmotor.set(GEAR_REVERSE_SPEED);
}
//climb() after the rod has been retracted, the spool winds in the cable, so the robot can climb.
public void climb(){
  if (!extending) {
    leftSpoolMotor.set(SPOOL_SPEED);
    rightSpoolMotor.set(SPOOL_SPEED);
  gearmotor.set(0);
  }
}//stop() allows both motors to immedediatly stop spinning 
public void stop(){
  gearmotor.set(0);
  leftSpoolMotor.set(0);
  rightSpoolMotor.set(0);
  extending = false;
}

}