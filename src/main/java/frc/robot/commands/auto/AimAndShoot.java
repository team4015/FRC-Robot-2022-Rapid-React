/* ==================================================
 * Authors: Lucas Jacobs
 * --------------------------------------------------
 * Description:
 * This class makes the robot aim at the target and
 * spin the shooter at the same time
 * ================================================== */

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Robot;

import frc.robot.commands.vision.VisionAim;

public class AimAndShoot extends ParallelCommandGroup {
    public AimAndShoot(Robot robot) {
        super(
            new VisionAim(robot),
            new AutoVisionShootTimed(robot, 5)
        );
    }
}
