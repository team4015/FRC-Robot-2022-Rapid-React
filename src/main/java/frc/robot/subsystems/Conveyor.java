/* ==================================================
 * Authors: Fawaaz Kamali Siddiqui, Lemi Miyu, 
 * Muhammad Ahmad Aniq Nomaan
 * --------------------------------------------------
 * Description: Contains functions and objects to control
 * the conveyor susbystem. It also contains code to prevent
 * jams from ocurring inside the ball storage bay.
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Conveyor extends SubsystemBase
{
  // HARDWARE //
  private PWMSparkMax conveyorMotor;
  private DigitalInput intakeSwitch;
  private DigitalInput conveyorSwitch;
  private DigitalInput shooterSwitch;
  private Timer timer;
  // declare motor controllers, solenoids, and other hardware here

  // CONSTANTS //
  public static final int CONVEYOR_SPARK = 9; // This is going to be finalized later.
  public static final int INTAKE_SWITCH = 0; // This is going to be finalized later.
  public static final int CONVEYOR_SWITCH = 1; // This is going to be finlaized later.
  public static final int SHOOTER_SWITCH = 2; // This is going to be finalized later.
  public static final int CONVEYOR_TIMEOUT = 2; // This is going to be finalized later.
  public static final double CONVEYOR_SPEED = 0.25; // This is going to be finalized later.
  public static final double CONVEYOR_REVERSE_SPEED = -0.25; // This is going to be finalized later.




  // CONSTRUCTOR //

  public Conveyor()
  {
    // instantiate hardware
    timer = new Timer();
    conveyorMotor = new PWMSparkMax(CONVEYOR_SPARK);
    intakeSwitch = new DigitalInput(INTAKE_SWITCH);
    conveyorSwitch = new DigitalInput(CONVEYOR_SWITCH);
    shooterSwitch = new DigitalInput(SHOOTER_SWITCH);

  }

  // METHODS //
  public boolean getIntakeSwitch() {
    return intakeSwitch.get();
  }

  public boolean getConveyorSwitch() {
    return conveyorSwitch.get();
  }

  public boolean getShooterSwitch() {
    return shooterSwitch.get();
  }

  /*
  standy() allows the conveyor belt to be on 
  a standby mode. The conveyor will only spin 
  unless a ball is given in it. If the bay is full, 
  this function also contains code to prevent the 
  loading of more new balls. 
  */


  public void standby(boolean shooterRunning) {
    if(!shooterSwitch.get() && intakeSwitch.get()) {
      feed(shooterRunning);

      timer.reset();
      timer.start();
      while(!conveyorSwitch.get() && timer.get() < CONVEYOR_TIMEOUT);

<<<<<<< Updated upstream
      timer.reset();
      timer.start();
      while(!conveyorSwitch.get() && timer.get() < CONVEYOR_TIMEOUT);
=======
<<<<<<< Updated upstream
=======
      timer.reset();
      timer.start();
      while(conveyorSwitch.get() && timer.get() < CONVEYOR_TIMEOUT);
>>>>>>> Stashed changes

      timer.stop();
    }

    stop();
  }

  /*
  feed() is the function that will spin 
  the conveyor to intake balls into the bay.
  */

  public void feed(boolean shooterRunning) {
    if(!shooterSwitch.get() || shooterRunning) {
      conveyorMotor.set(CONVEYOR_SPEED);
    } else {
      stop();
    }
  }


  public void stop() { // This function stops the conveyor from running.
    conveyorMotor.set(0);
  }

  public void reverse() { // This function will reverse the conveyor to outtake balls.
    conveyorMotor.set(CONVEYOR_REVERSE_SPEED);
  }
<<<<<<< Updated upstream
=======
>>>>>>> Stashed changes
>>>>>>> Stashed changes

}
