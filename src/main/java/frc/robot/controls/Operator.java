/* ==================================================
 * Authors: Lucas Jacobs and Jason Wang
 *
 * --------------------------------------------------
 * Description:
 * This class contains most of the code that handles
 * button binding, joystick reading, and command binding.
 *
 * The "operator" is the secondary drive team member that
 * controls all other robot subsystems other than the wheels.
 * These controls allow the operator to use the subsystems.
 * ================================================== */

package frc.robot.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;
import frc.robot.commands.intake.*;
import frc.robot.commands.climber.*;
import frc.robot.commands.conveyor.*;
import frc.robot.commands.vision.*;
import frc.robot.commands.shooter.*;
import frc.robot.Robot;

public class Operator
{
    // JOYSTICKS / CONTROLLERS //
    private Joystick dualshock;

    // BUTTONS //
    private JoystickButton intakeIn;
    private JoystickButton intakeOut;

    private JoystickButton intakeFlip;

    private JoystickButton conveyorFeed;
    private JoystickButton conveyorReverse;

    private JoystickButton climberClimb;
    private JoystickButton climberUnwind;
    private JoystickButton climberExtend;
    private JoystickButton climberRetract;

    private JoystickButton visionAim;

    private POVButton shooterLow;
    private POVButton shooterMed;
    private POVButton shooterHigh;
    private POVButton shooterExHigh;

	  // PORTS //

    public static final int DUALSHOCK = 2;

    public static final int INTAKE_IN = 8;
    public static final int INTAKE_OUT = 6;

    public static final int INTAKE_FLIP = 10;

    public static final int CONVEYOR_FEED = 7;
    public static final int CONVEYOR_REVERSE = 5;

    public static final int CLIMBER_CLIMB = 1;
    public static final int CLIMBER_UNWIND = 4;
    public static final int CLIMBER_EXTEND = 3;
    public static final int CLIMBER_RETRACT = 2;

    public static final int VISION_AIM = 9;

    public static final int SHOOTER_LOW = 90;
    public static final int SHOOTER_MED = 180;
    public static final int SHOOTER_HIGH = 270;
    public static final int SHOOTER_EX_HIGH = 0;
  
    // CONSTANTS //
    public static final double SHOOTER_DEADZONE = 0.1;


    public Operator(Robot robot)
    {
		// instantiate joysticks / controllers
        dualshock = new Joystick(DUALSHOCK);

        // bind button objects to physical buttons
        intakeIn = new JoystickButton(dualshock, INTAKE_IN);
        intakeOut = new JoystickButton(dualshock, INTAKE_OUT);

        //intakeFlip = new JoystickButton(dualshock, INTAKE_FLIP);

        conveyorFeed = new JoystickButton(dualshock, CONVEYOR_FEED);
        conveyorReverse = new JoystickButton(dualshock, CONVEYOR_REVERSE);

        climberClimb = new JoystickButton(dualshock, CLIMBER_CLIMB);
        climberUnwind = new JoystickButton(dualshock, CLIMBER_UNWIND);
        climberExtend = new JoystickButton(dualshock, CLIMBER_EXTEND);
        climberRetract = new JoystickButton(dualshock, CLIMBER_RETRACT);

        visionAim = new JoystickButton(dualshock, VISION_AIM);

        shooterLow = new POVButton(dualshock, SHOOTER_LOW);
        shooterMed = new POVButton(dualshock, SHOOTER_MED);
        shooterHigh = new POVButton(dualshock, SHOOTER_HIGH);
        shooterExHigh = new POVButton(dualshock, SHOOTER_EX_HIGH);

        // bind buttons to commands
        intakeIn.whileHeld(new IntakeSpin(robot));
        intakeOut.whileHeld(new IntakeReverse(robot));

        //DONT ACCIDENTALLY FLIP AT HUMBER

        // intakeFlip.whenPressed(new IntakeFlip(robot));

        conveyorFeed.whileHeld(new ConveyorFeed(robot));
        conveyorReverse.whileHeld(new ConveyorReverse(robot));

        climberClimb.whileHeld(new ClimberClimb(robot));
        climberUnwind.whileHeld(new ClimberUnwind(robot));
        climberExtend.whileHeld(new ClimberExtend(robot));
        climberRetract.whileHeld(new ClimberRetract(robot));
      
        visionAim.whileHeld(new VisionAim(robot));
        
        shooterLow.whileHeld(new ShooterSpin(robot, 0.4));
        shooterMed.whileHeld(new ShooterSpin(robot, 0.44));
        shooterHigh.whileHeld(new ShooterSpin(robot, 0.48));
        shooterExHigh.whileHeld(new ShooterSpin(robot, 0.52));
    }
       // METHODS

    // Add methods here which return values for various robot controls by reading the controllers.

    /* Authors: Jason Wang & Lucas Jacobs*
    * Desc: NOTNEEDED FOR HUMBER
    * Returns the power given to the shooter by reading the left
    * joystick of the controller*/

    /*public double getShooterPower(){
        double power = -dualshock.getY();

        if (Math.abs(power) < SHOOTER_DEADZONE) power = 0;

        return power;
    }*/
}

