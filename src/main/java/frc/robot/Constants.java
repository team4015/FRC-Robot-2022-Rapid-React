/* ==================================================
Authour: Shane Pinto & Lucas Jacobs
Description: Constants.java contains most of the 
constant values used throughout the robot. This 
includes settings, hardware ports, and other values.
================================================== */

package frc.robot;

public final class Constants
{
    // HARDWARE PORTS
    public static final int INTAKE_SPARK = -1;
    public static final int INTAKE_PISTON_DEPLOY = -1;
    public static final int INTAKE_PISTON_RETRACT = -1;
   

    // CONTROLLER COMPUTER PORTS
    public static final int DUALSHOCK = 0;

    // LIMIT SWITCH PORTS


    // SETTINGS
    public static final double DEADZONE = 0.15; // Deadzone applied to joysticks to aid in adjusting sensitivity
    public static final double MAX_THROTTLE_SPEED = 0.30;
    public static final double MAX_STEER_SPEED = 0.2;
    public static final double INTAKE_SPEED = 0.2;
    public static final double INTAKE_REVERSE_SPEED = -0.2;

    // TARGETING CONSTANTS

}
