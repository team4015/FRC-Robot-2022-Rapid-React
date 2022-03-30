/* ==================================================
 * Authors: Lucas Jacobs
 * --------------------------------------------------
 * Description:
 * This command group shoots the ball into the top hub 
 * assuming the front of the robot is pressed against the wall
 * closest to the hub.
 * ================================================== */

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Robot;

public class AutoWallShoot extends ParallelCommandGroup {
    public AutoWallShoot(Robot robot) {
        super(
            new AutoShoot(robot, .37, 1) //shoot
        );
    }
}
