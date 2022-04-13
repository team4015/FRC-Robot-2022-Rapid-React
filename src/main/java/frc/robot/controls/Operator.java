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
import frc.robot.commands.auto.*;
import frc.robot.Robot;

public class Operator
{
    Robot robot;

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

    private JoystickButton autoShoot;

    private POVButton shooterRed;
    private POVButton shooterOrange;
    private POVButton shooterGreen;
    private POVButton shooterBlue;
    private POVButton shooterViolet;

    //private POVButton shooterHub;
    //private POVButton shooterSpin;
    //private POVButton shooterWall;
    private POVButton shooterReverse;

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

    public static final int AUTO_SHOOT = 9;

    public static final int SHOOTER_RED = 90;
    public static final int SHOOTER_ORANGE = 135;
    public static final int SHOOTER_GREEN = 180;
    public static final int SHOOTER_BLUE = 225;
    public static final int SHOOTER_VIOLET = 270;

    //public static final int SHOOTER_HUB = 90;
    //public static final int SHOOTER_SPIN = 180;
    //public static final int SHOOTER_WALL = 270;
    public static final int SHOOTER_REVERSE = 0;
  
    // CONSTANTS //
    public static final double SHOOTER_DEADZONE = 0.1;

    //public static final double SHOOTER_SPEED_LOW = .4;
    public static final double SHOOTER_SPEED_SPIN = .44;
    //public static final double SHOOTER_SPEED_HIGH = .48;
    public static final double SHOOTER_SPEED_REVERSE = -.3;



    public Operator(Robot robot)
    {
        this.robot=robot;
		// instantiate joysticks / controllers
        dualshock = new Joystick(DUALSHOCK);

        // bind button objects to physical buttons
        intakeIn = new JoystickButton(dualshock, INTAKE_IN);
        intakeOut = new JoystickButton(dualshock, INTAKE_OUT);

        intakeFlip = new JoystickButton(dualshock, INTAKE_FLIP);

        conveyorFeed = new JoystickButton(dualshock, CONVEYOR_FEED);
        conveyorReverse = new JoystickButton(dualshock, CONVEYOR_REVERSE);

        climberClimb = new JoystickButton(dualshock, CLIMBER_CLIMB);
        climberExtend = new JoystickButton(dualshock, CLIMBER_EXTEND);
        climberRetract = new JoystickButton(dualshock, CLIMBER_RETRACT);

        autoShoot = new JoystickButton(dualshock, AUTO_SHOOT);

        shooterRed = new POVButton(dualshock, SHOOTER_RED);
        shooterOrange = new POVButton(dualshock, SHOOTER_ORANGE);
        shooterGreen = new POVButton(dualshock, SHOOTER_GREEN);
        shooterBlue = new POVButton(dualshock, SHOOTER_BLUE);
        shooterViolet = new POVButton(dualshock, SHOOTER_VIOLET);

        //shooterHub = new POVButton(dualshock, SHOOTER_HUB);
        //shooterSpin = new POVButton(dualshock, SHOOTER_SPIN);
        //shooterWall = new POVButton(dualshock, SHOOTER_WALL);
        shooterReverse = new POVButton(dualshock, SHOOTER_REVERSE);

        // bind buttons to commands
        intakeIn.whileHeld(new IntakeSpin(robot));
        intakeOut.whileHeld(new IntakeReverse(robot));

        //DONT ACCIDENTALLY FLIP AT HUMBER

        intakeFlip.whenPressed(new IntakeFlip(robot));

        conveyorFeed.whileHeld(new ConveyorFeed(robot));
        conveyorReverse.whileHeld(new ConveyorReverse(robot));

        climberClimb.whileHeld(new ClimberClimb(robot));
        climberExtend.whileHeld(new ClimberExtend(robot));
        climberRetract.whileHeld(new ClimberRetract(robot));
      
        autoShoot.whileHeld(new AutoVisionShoot(robot, -1));
        
         /*
            WIDTH   SPEED
            55      5.2 RED
            58      4.8 ORANGE
            60      5 -----------------
            65      4.5 GREEN
            70      4.4 BLUE
            80      4.3 VIOLET

            */

        shooterRed.whileHeld(new AutoVisionShoot(robot, 4.132));
        shooterOrange.whileHeld(new AutoVisionShoot(robot, 4.142));
        shooterGreen.whileHeld(new AutoVisionShoot(robot, 4.258));
        shooterBlue.whileHeld(new AutoVisionShoot(robot, 4.48));
        shooterViolet.whileHeld(new AutoVisionShoot(robot, 4.785));

        //shooterHub.whenPressed(new AutoHubShoot(robot));
        //shooterSpin.whileHeld(new ShooterSpin(robot, SHOOTER_SPEED_SPIN));
        //shooterWall.whenHeld(new AutoWallShoot(robot));
        shooterReverse.whileHeld(new ShooterSpin(robot, SHOOTER_SPEED_REVERSE));
    }
       // METHODS

    // Add methods here which return values for various robot controls by reading the controllers.

public void enableUnwind() {
    climberUnwind = new JoystickButton(dualshock, CLIMBER_UNWIND);
    climberUnwind.whileHeld(new ClimberUnwind(robot));
}

public void disableUnwind() {
    climberUnwind = null;
}
}