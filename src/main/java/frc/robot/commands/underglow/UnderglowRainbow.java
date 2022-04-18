/* ==================================================
 * Authors:
 * Kyle Pinto
 * --------------------------------------------------
 * Description:
 * Cycles through the RGB colour spectrum.
 * ================================================== */

package frc.robot.commands.underglow;

import frc.robot.Robot;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class UnderglowRainbow extends CommandBase
{
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})

  // VARIABLES //

  private Robot robot;

  private int red;
  private int green;
  private int blue;

  private int red_target;
  private int green_target;
  private int blue_target;

  // CONSTANTS //



  // CONSTRUCTOR //

  public UnderglowRainbow(Robot robot)
  {
    this.robot = robot;
    addRequirements(robot.underglow);

    red = 0;
    green = 0;
    blue = 0;

    red_target = 0;
    green_target = 0;
    blue_target = 0;
  }

  // METHODS //

  private void updateColour()
  {
    robot.underglow.setColour(red, green, blue);
  }

  private void updateTargets()
  {
    if (red == 255 && green == 0 && blue == 0)
    {
      green_target = 255;
    }
    else if (red == 255 && green == 255 && blue == 0)
    {
      red_target = 0;
    }
    else if (red == 0 && green == 255 && blue == 0)
    {
      blue_target = 255;
    }
    else if (red == 0 && green == 255 && blue == 255)
    {
      green_target = 0;
    }
    else if (red == 0 && green == 0 && blue == 255)
    {
      red_target = 255;
    }
    else if (red == 255 && green == 0 && blue == 255)
    {
      blue_target = 0;
    }
  }

  private void updateBrightnesses()
  {
    if (red < red_target)
    {
      red++;
    }
    else if (red > red_target)
    {
      red--;
    }

    if (green < green_target)
    {
      green++;
    }
    else if (green > green_target)
    {
      green--;
    }

    if (blue < blue_target)
    {
      blue++;
    }
    else if (blue > blue_target)
    {
      blue--;
    }
  }

  @Override
  public void initialize()
  {
    robot.underglow.off();

    red = 255;
    green = 0;
    blue = 0;

    red_target = 255;
    green_target = 0;
    blue_target = 0;
  }

  @Override
  public void execute()
  {
    updateTargets();
    updateBrightnesses();
    updateColour();
  }

  @Override
  public void end(boolean interrupted)
  {
    robot.underglow.off();
  }

  @Override
  public boolean isFinished()
  {
    return false;
  }
}
