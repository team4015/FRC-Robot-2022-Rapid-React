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
import frc.robot.Constants;

public class Intake extends SubsystemBase
{
  // HARDWARE //

  private PWMSparkMax intakeMotor;
  private DoubleSolenoid intakePiston;
  private boolean deployed;

  public Intake()
  {
    // instantiate hardware
    intakeMotor = new PWMSparkMax(Constants.INTAKE_SPARK); // The intake is run by one brushless motor controlled by one SparkMax motor controller
    intakePiston = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.INTAKE_PISTON_DEPLOY, Constants.INTAKE_PISTON_RETRACT);
    deployed = false;
  }

  // METHODS //

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
      intakeMotor.set(Constants.INTAKE_SPEED);
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
      intakeMotor.set(Constants.INTAKE_REVERSE_SPEED);
    }
  }
}
