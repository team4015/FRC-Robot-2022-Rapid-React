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
import frc.robot.commands.*;
import frc.robot.Robot;

public class Operator
{
    // JOYSTICKS / CONTROLLERS //
    private Joystick dualshock;

    // PORTS //
    public static final int DUALSHOCK = 2;

    // BUTTONS //


	// CONSTANTS //



    public Operator(Robot robot)
    {
		// instantiate joysticks / controllers
        dualshock = new Joystick(DUALSHOCK);

        // bind button objects to physical buttons

        // bind buttons to commands
    }

	// METHODS //

    // Add methods here which return values for various robot controls by reading the controllers.
}
