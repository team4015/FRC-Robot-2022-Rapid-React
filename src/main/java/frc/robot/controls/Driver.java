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
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.vision.*;
import frc.robot.Robot;

public class Driver
{   // JOYSTICKS / CONTROLLERS //
    private Joystick throttle;
    private Joystick steer;

    public static final int THROTTLE = 0;
    public static final int STEER = 1;


    // BUTTONS //
    public static final int RUPPERUP = 0;
    public static final int RUPPERDOWN = 1;
    public static final int RLOWERUP = 0;
    public static final int RLOWERDOWN = 1;
    public static final int GUPPERUP = 2;
    public static final int GUPPERDOWN = 3;
    public static final int GLOWERUP = 2;
    public static final int GLOWERDOWN = 3;
    public static final int BUPPERUP = 4;
    public static final int BUPPERDOWN = 5;
    public static final int BLOWERUP = 4;
    public static final int BLOWERDOWN = 5;
    
    JoystickButton rUpperUp;
    JoystickButton rUpperDown;
    JoystickButton rLowerUp;
    JoystickButton rLowerDown;
    JoystickButton gUpperUp;
    JoystickButton gUpperDown;
    JoystickButton gLowerUp;
    JoystickButton gLowerDown;
    JoystickButton bUpperUp;
    JoystickButton bUpperDown;
    JoystickButton bLowerUp;
    JoystickButton bLowerDown;



	// CONSTANTS //
    public static final double DEADZONE = 0.15; // Deadzone applied to joysticks to aid in adjusting sensitivity
    public static final double MAX_THROTTLE_SPEED = 0.7;
    public static final double MAX_STEER_SPEED = 0.5;


    public Driver(Robot robot)
    {
		// instantiate joysticks / controllers
        throttle = new Joystick(THROTTLE);
        steer = new Joystick(STEER);

        // bind button objects to physical buttons
       rUpperUp = new JoystickButton(throttle, RUPPERUP);
    rUpperDown = new JoystickButton(throttle, RUPPERDOWN);
     rLowerUp = new JoystickButton(steer, RLOWERUP);
     rLowerDown = new JoystickButton(steer, RLOWERDOWN);
     gUpperUp = new JoystickButton(throttle, GUPPERUP);
     gUpperDown = new JoystickButton(throttle, GUPPERDOWN);
     gLowerUp = new JoystickButton(steer, GLOWERUP);
     gLowerDown = new JoystickButton(steer, GLOWERDOWN);
     bUpperUp = new JoystickButton(throttle, BUPPERUP);
     bUpperDown = new JoystickButton(throttle, BUPPERDOWN);
     bLowerUp = new JoystickButton(steer, BLOWERUP);
     bLowerDown = new JoystickButton(steer, BLOWERDOWN);

        // bind buttons to commands
        rUpperUp.whenPressed( new AddRedUp(robot, 1));
    rUpperDown.whenPressed( new AddRedUp(robot, -1));
     rLowerUp.whenPressed( new AddRedDown(robot, 1));
     rLowerDown.whenPressed( new AddRedDown(robot, -1));
     gUpperUp.whenPressed( new AddGreenUp(robot, 1));
     gUpperDown.whenPressed( new AddGreenUp(robot, -1));
     gLowerUp.whenPressed( new AddGreenDown(robot, 1));
     gLowerDown.whenPressed( new AddGreenDown(robot, -1));
     bUpperUp.whenPressed( new AddBlueUp(robot, 1));
     bUpperDown.whenPressed( new AddBlueUp(robot, -1));
     bLowerUp.whenPressed( new AddBlueDown(robot, 1));
     bLowerDown.whenPressed( new AddBlueDown(robot, -1));

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
