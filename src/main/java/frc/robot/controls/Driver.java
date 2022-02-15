/* ==================================================
 * Authors: Lucas Jacobs, Shane Pinto
 *
 * --------------------------------------------------
 * Description:
 * This class contains most of the code that handles
 * button binding, joystick reading, and command binding.
 *
 * The "driver" is the primary drive team member that
 * drives the robot around the field.  These controls
 * are used by the main driver to move the robot's wheels.
 * ================================================== */

package frc.robot.controls;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.*;
import frc.robot.Constants;
import frc.robot.commands.*;
import frc.robot.Robot;

public class Driver
{
    // JOYSTICKS / CONTROLLERS //
    private Joystick dualshock;


    // BUTTONS //



	// CONSTANTS //



    public Driver(Robot robot)
    {
		// instantiate joysticks / controllers
        dualshock = new Joystick(Constants.DUALSHOCK);

        // bind button objects to physical buttons

        // bind buttons to commands
    }

	// METHODS //

	// Add methods here which return values for various robot controls by reading the controllers.

    /* =====================================
    getThrottle() will return the current
    value of the Y-Axis of the left stick on the controller
    It will also handle adjusting
    the reading according to the deadzone,
    and according to the max throttle speed.
    ===================================== */
    public double getThrottle()
    {
        double throttleValue = 0;
        
        throttleValue = -dualshock.getY();
        throttleValue *= Constants.MAX_THROTTLE_SPEED;

        if (Math.abs(throttleValue) < Constants.DEADZONE)
        {
            return 0;
        }

        return throttleValue;
    }

    /* =====================================
    getSteer() will return the current
    value of the X-Axis of the right stick on the controller
    joystick. It will also handle adjusting
    the reading according to the deadzone,
    and according to the max steer speed.
    ===================================== */
    public double getSteer()
    {
        double steerValue = 0;

        steerValue = dualshock.getZ();
        steerValue *= Constants.MAX_STEER_SPEED;

        if (Math.abs(steerValue) < Constants.DEADZONE)
        {
            return 0;
        }

        return steerValue;
    }
}
