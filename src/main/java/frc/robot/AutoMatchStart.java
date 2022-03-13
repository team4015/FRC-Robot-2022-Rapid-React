/* ==================================================
 * Authors: Lucas Jacobs
 * --------------------------------------------------
 * Description:
 * This class contains the commands that are executed 
 * in autonomous mode at the start of the match
 * 
 * ***Runs in the Autonomous Period Only***
 * ================================================== */

package frc.robot;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.auto.*;

public class AutoMatchStart extends SequentialCommandGroup {
    public AutoMatchStart(Robot robot) {
        super(
            new AutoDrive(robot, -.5, 0, 2), // drive backwards
            new AutoShoot(robot, .4, 3) //shoot

            // Robot on right tarmac

            //new AutoDrive(robot, 0, -.5, 1.2), //turn left
            //new AutoDrive(robot, .5, 0, 1.5) //drive away

            //Robot on left tarmac
      
            //new AutoDrive(robot, 0, .5, 1.2), //turn right
            //new AutoDrive(robot, .7, 0, 1.2) //drive away
        );
    }
}
