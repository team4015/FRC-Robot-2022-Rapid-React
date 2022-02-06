/* ==================================================
 * Authors:
 * FIRST
 * --------------------------------------------------
 * Description:
 * This class instantiates the Match.java class.
 * It also contains main().
 * ================================================== */

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main
{
  private Main() {}

  public static void main(String... args)
  {
    RobotBase.startRobot(Match::new);
  }
}
