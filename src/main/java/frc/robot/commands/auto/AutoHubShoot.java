/* ==================================================
 * Authors: Lucas Jacobs
 * --------------------------------------------------
 * Description:
 * This command group shoots the ball into the top hub 
 * assuming the front of the robot is pressed agains the hub.
 * 
 * The distance from the fender to the centre of the hub is 86 cm
 * (From Game Manual)
 * ================================================== */

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;

public class AutoHubShoot extends SequentialCommandGroup {
    public AutoHubShoot(Robot robot) {
        super(
            new AutoDrive(robot, -.8, 0, .6), // drive backwards
            new AutoShoot(robot, .38, .8) //shoot
        );
    }
}
