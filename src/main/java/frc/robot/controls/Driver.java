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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Robot;
import frc.robot.commands.driver.HighSpeed;
import frc.robot.commands.driver.LowSpeed;
import frc.robot.commands.vision.VisionAim;

public class Driver
{
    // JOYSTICKS / CONTROLLERS //
    private Joystick throttle;
    private Joystick steer;

    public static final int THROTTLE = 0;
    public static final int STEER = 1;


    // BUTTONS //
    private JoystickButton lowSpeed;
    private JoystickButton highSpeed;
    private JoystickButton aim;


	// CONSTANTS //
    public static final double DEADZONE = 0.15; // Deadzone applied to joysticks to aid in adjusting sensitivity
    public static final double THROTTLE_LOW_SPEED = 0.5;
    public static final double STEER_LOW_SPEED = 0.4;
    public static final double THROTTLE_HIGH_SPEED = .8;
    public static final double STEER_HIGH_SPEED = .8;

    public static final int LOW_SPEED = 5;
    public static final int HIGH_SPEED = 3;
    public static final int AIM = 2;

    public double throttleSpeed = 0.8;
    public double steerSpeed = .7;


    public Driver(Robot robot)
    {
		// instantiate joysticks / controllers
        throttle = new Joystick(THROTTLE);
        steer = new Joystick(STEER);

        // bind button objects to physical buttons
        lowSpeed = new JoystickButton(throttle, LOW_SPEED);
        highSpeed = new JoystickButton(throttle, HIGH_SPEED);
        aim = new JoystickButton(steer, AIM);

        // bind buttons to commands
        lowSpeed.whenPressed(new LowSpeed(robot));
        highSpeed.whenPressed(new HighSpeed(robot));
        aim.whileHeld(new VisionAim(robot));
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

        throttleValue *= throttleSpeed;

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

        steerValue *= steerSpeed;

        return steerValue;
    }

    /* Author: Lucas Jacobs
    *  Desc: Sets the drivetrain to high speed */

    public void useLowSpeed() {
        throttleSpeed = THROTTLE_LOW_SPEED;
        steerSpeed = STEER_LOW_SPEED;
        SmartDashboard.putString("Drive Speed", "LOW");
    }

    /* Author: Lucas Jacobs
    *  Desc: Sets the drivetrain to low speed */
    public void useHighSpeed() {
        throttleSpeed = THROTTLE_HIGH_SPEED;
        steerSpeed = STEER_HIGH_SPEED;
        SmartDashboard.putString("Drive Speed", "HIGH");
    }
}
