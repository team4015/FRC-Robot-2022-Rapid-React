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
import frc.robot.Robot;

public class Driver
{
    // JOYSTICKS / CONTROLLERS //
    private Joystick throttle;
    private Joystick steer;

    public static final int THROTTLE = 0;
    public static final int STEER = 1;


    // BUTTONS //



	// CONSTANTS //
    public static final double DEADZONE = 0.15; // Deadzone applied to joysticks to aid in adjusting sensitivity
    public static final double MAX_THROTTLE_SPEED = 0.30;
    public static final double MAX_STEER_SPEED = 0.2;


    public Driver(Robot robot)
    {
		// instantiate joysticks / controllers
        throttle = new Joystick(THROTTLE);
        steer = new Joystick(STEER);

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
        double throttleValue = -throttle.getY();
        if (Math.abs(throttleValue) < DEADZONE) return 0;

        throttleValue *= MAX_THROTTLE_SPEED;

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
        double steerValue =  steer.getX();
        if (Math.abs(steerValue) < DEADZONE) return 0;

        steerValue *= MAX_STEER_SPEED;

        return steerValue;
    }
}
