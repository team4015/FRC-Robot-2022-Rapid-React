/* ==================================================
 * Authors: Lucas Jacobs
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
import frc.robot.commands.conveyor.*;
import frc.robot.Robot;

public class Operator
{
    // JOYSTICKS / CONTROLLERS //
    private Joystick dualshock;

    // BUTTONS //
    private JoystickButton intakeIn;
    private JoystickButton intakeOut;

    private JoystickButton intakeDeploy;
    private JoystickButton intakeRetract;

    private JoystickButton conveyorFeed;
    private JoystickButton conveyorReverse;

	// CONSTANTS //
    public static final int DUALSHOCK = 2; 

    public static final int INTAKE_IN = 8;
    public static final int INTAKE_OUT = 6;

    public static final int INTAKE_DEPLOY = 10;
    public static final int INTAKE_RETRACT = 9;

    public static final int CONVEYOR_FEED = 7;
    public static final int CONVEYOR_REVERSE = 5;


    public Operator(Robot robot)
    {
		// instantiate joysticks / controllers
        dualshock = new Joystick(DUALSHOCK);

        // bind button objects to physical buttons
        intakeIn = new JoystickButton(dualshock, INTAKE_IN);
        intakeOut = new JoystickButton(dualshock, INTAKE_OUT);

        intakeDeploy = new JoystickButton(dualshock, INTAKE_DEPLOY);
        intakeRetract = new JoystickButton(dualshock, INTAKE_RETRACT); 

        conveyorFeed = new JoystickButton(dualshock, CONVEYOR_FEED);
        conveyorReverse = new JoystickButton(dualshock, CONVEYOR_REVERSE);

        // bind buttons to commands
        intakeIn.whenHeld(new IntakeSpin(robot));
        intakeOut.whenHeld(new IntakeReverse(robot));

        intakeDeploy.whenPressed(new IntakeDeploy(robot));
        intakeRetract.whenPressed(new IntakeRetract(robot));

        conveyorFeed.whenHeld(new ConveyorFeed(robot));
        conveyorReverse.whenHeld(new ConveyorReverse(robot));
    }

	// METHODS //

    // Add methods here which return values for various robot controls by reading the controllers.
}
