/* ==================================================
 * Authors: Lucas Jacobs
 * --------------------------------------------------
 * Description:
 * This class caontinds the commands that are executed 
 * in autonomous mode at the start of the match
 * ================================================== */

package frc.robot;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.*;

public class AutoGroup extends SequentialCommandGroup {
    public AutoGroup(Robot robot) {
        super(
            new AutoDrive(robot, -.5, 0, 2), // drive backwards
            new AutoShoot(robot, 3, .4) //shoot

            // Robot on right tarmac

            //new AutoDrive(robot, 0, -.5, 1.2), //turn left
            //new AutoDrive(robot, .5, 0, 1.5) //drive away

            //Robot on left tarmac
      
            //new AutoDrive(robot, 0, .5, 1.2), //turn right
            //new AutoDrive(robot, .7, 0, 1.2) //drive away
        );
    }
}
