/* ==================================================
 * Authors: Lucas Jacobs
 * --------------------------------------------------
 * Description:
 * This class contains the commands that are executed 
 * in autonomous mode at the start of the match.
 * 
 * Picks up a ball, turns around, and shoots two
 * 
 * ***Runs in the Autonomous Period Only***
 * ================================================== */

package frc.robot.commands.auto.startMatch;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.auto.*;
import frc.robot.commands.intake.IntakeDeploy;
import frc.robot.commands.intake.IntakeSpinForever;
import frc.robot.commands.intake.IntakeStop;
import frc.robot.commands.intake.StopIntakeSpinForever;

public class TwoBallAuto extends SequentialCommandGroup {

    private Robot robot;

    public TwoBallAuto(Robot robot) { 
        super(
            new IntakeDeploy(robot),
            new IntakeSpinForever(robot),
            new DriveDistance(robot, 1.6),
            new Wait(2),
            new StopIntakeSpinForever(robot),
            new TurnAngle(robot, 0.01, 180),
           new AimAndShoot(robot)
        );
        this.robot = robot;
    }

    @Override
    public void end(boolean interrupted) {
        robot.intake.setDefaultCommand(new IntakeStop(robot));
    }
}
