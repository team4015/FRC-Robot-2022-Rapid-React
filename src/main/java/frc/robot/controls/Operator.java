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

    private JoystickButton shooterSpin;

	// CONSTANTS //
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

    public static final int SHOOTER_SPIN = 9;


    public Operator(Robot robot)
    {
		// instantiate joysticks / controllers
        dualshock = new Joystick(DUALSHOCK);

        // bind button objects to physical buttons
        intakeIn = new JoystickButton(dualshock, INTAKE_IN);
        intakeOut = new JoystickButton(dualshock, INTAKE_OUT);

        intakeFlip = new JoystickButton(dualshock, INTAKE_FLIP);

        conveyorFeed = new JoystickButton(dualshock, CONVEYOR_FEED);
        conveyorReverse = new JoystickButton(dualshock, CONVEYOR_REVERSE);

        climberClimb = new JoystickButton(dualshock, CLIMBER_CLIMB);
        climberUnwind = new JoystickButton(dualshock, CLIMBER_UNWIND);
        climberExtend = new JoystickButton(dualshock, CLIMBER_EXTEND);
        climberRetract = new JoystickButton(dualshock, CLIMBER_RETRACT);

        shooterSpin = new JoystickButton(dualshock, SHOOTER_SPIN);

        // bind buttons to commands
        intakeIn.whenHeld(new IntakeSpin(robot));
        intakeOut.whenHeld(new IntakeReverse(robot));

        intakeFlip.whenPressed(new IntakeFlip(robot));

        conveyorFeed.whenHeld(new ConveyorFeed(robot));
        conveyorReverse.whenHeld(new ConveyorReverse(robot));

        climberClimb.whenHeld(new ClimberClimb(robot));
        climberUnwind.whenHeld(new ClimberUnwind(robot));
        climberExtend.whenHeld(new ClimberExtend(robot));
        climberRetract.whenHeld(new ClimberRetract(robot));

        shooterSpin.whenHeld(new ShooterSetSpeed(robot));
        shooterSpin.whenReleased(new ShooterSpin(robot));
        // METHODS
    // Add methods here which return values for various robot controls by reading the controllers.
    }
}
