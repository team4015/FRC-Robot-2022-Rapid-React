/* ==================================================
 * Authors:
 *
 * --------------------------------------------------
 * Description:
 * This class that contains all the necessary things
 * to make a robot instance. This class will make
 * instances of each subsystem, initialize a startup
 * function for each subsystem, and then set a default
 * command for each subsystem to use. It will also
 * instantiate Driver.java to allow driver input via
 * joysticks to control the robot.
 * ================================================== */

package frc.robot;

import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

public class Robot
{
  // SUBSYSTEMS //

  public ExampleSubsystem exampleSubsystem;

  // CONSTRUCTOR //

  public Robot()
  {
    // instantiate subsystems
    exampleSubsystem = new ExampleSubsystem();

    initialize();
    setDefaultCommands();
  }

  // METHODS //

  /* -------------------------------------
   * initialize() will tell each
   * subsystem to perform a starting function.
   * Often this is to just stop all motors
   * and set the robot to a starting position.
   * ------------------------------------- */
  private void initialize()
  {

  }

  /* -------------------------------------
   * setDefaultCommands() will tell each
   * subsystem which command should be run
   * when no other command is being scheduled.
   * ------------------------------------- */
  private void setDefaultCommands()
  {
    exampleSubsystem.setDefaultCommand(new ExampleCommand(this));
  }
}
