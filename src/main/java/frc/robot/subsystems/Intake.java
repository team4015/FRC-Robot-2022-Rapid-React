/* ==================================================
 * Authors: Lucas Jacobs & Shane Pinto
 *
 * --------------------------------------------------
 * Description:
 *
 * Contains the functions needed to control the intake
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public class Intake extends SubsystemBase
{
  // HARDWARE //
  private PWMSparkMax intakeMotor;
  private DoubleSolenoid intakePiston;
  private boolean deployed;

  // PORTS //
  public static final int INTAKE_SPARK = 4;
  public static final int INTAKE_PISTON_DEPLOY = 1;
  public static final int INTAKE_PISTON_RETRACT = 0;

  // CONSTANTS //
  public static final double INTAKE_SPEED = 0.2;
  public static final double INTAKE_REVERSE_SPEED = -0.2;

  public Intake()
  {
    // instantiate hardware
    intakeMotor = new PWMSparkMax(INTAKE_SPARK); // The intake is run by one brushless motor controlled by one SparkMax motor controller
    intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, INTAKE_PISTON_DEPLOY, INTAKE_PISTON_RETRACT);
    deployed = false;
  }

  // METHODS //

  /* =====================================
  flip() deploys the intake if it is 
  retracted and retracts the intake if 
  it is deployed
  ===================================== */
  public void flip() {
    if (deployed) {
      retract();
    } else {
      deploy();
    }
  }

  /* =====================================
  deploy() extends the intake piston so 
  that the intake hangs in front of the
  robot chassis.
  ===================================== */
  public void deploy()
  {
    intakePiston.set(Value.kForward);
    deployed = true;
  }

  /* =====================================
  retract() pulls the intake piston back
  in so that the intake is held up inside
  the robot chassis.
  ===================================== */
  public void retract()
  {
    intakePiston.set(Value.kReverse);
    deployed = false;
  }

  /* =====================================
  spin() sets the speed and direction of 
  the intake so that it intakes balls.
  ===================================== */
  public void spin()
  {
    if (deployed) // Prevents the intake from spinning when held up inside the chassis
    {
      intakeMotor.set(INTAKE_SPEED);
    }
  }

  /* =====================================
  stop() sets the speed and direction of 
  the intake so that it completely stops
  spinning.
  ===================================== */
  public void stop()
  {
    intakeMotor.set(0);
  }

  /* =====================================
  reverse() sets the speed and direction of 
  the intake so that it outakes balls.
  ===================================== */
  public void reverse()
  {
    if (deployed)
    {
      intakeMotor.set(INTAKE_REVERSE_SPEED);
    }
  }
}
