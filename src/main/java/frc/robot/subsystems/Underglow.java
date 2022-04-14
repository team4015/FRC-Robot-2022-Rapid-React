/* ==================================================
 * Authors:
 * Shane Pinto
 * Kyle Pinto
 * --------------------------------------------------
 * Description: Code for LED Light strips underneath
 * robot. Communicates via Serial using the RS-232
 * port (UART) on the RoboRIO.  Bytes will only be
 * transmitted to the RGB controller if busy()
 * returns false, meaning that there are no pending
 * bytes to send.
 *
 * RGB Controller Repository: https://github.com/team4015/RGB-Controller
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.colours.*;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Underglow extends SubsystemBase
{
  private static final int HEADER = 0xFF;
  private static final int BAUD_RATE = 9600;
  private static final double TIMEOUT = 100e-3;    // 100 ms
  private static final double TX_DELAY = 1.5e-3;   // 1.5 ms

  private SerialPort uart;
  private Timer timer;       // tracks the time between transmitted bytes
  private int index;         // index of the next byte to be transmitted
  private int [] buffer;     // data to be transmitted

  private Colours oldColour;

  private SendableChooser<Colours> colourOption;

  public Underglow()
  {
    uart = new SerialPort(BAUD_RATE, Port.kMXP, 8, Parity.kNone, StopBits.kOne);
    uart.reset();
    uart.setFlowControl(FlowControl.kNone);
    uart.setTimeout(TIMEOUT);

    timer = new Timer();
    timer.reset();
    timer.start();

    buffer = null;
    index = 0;

    oldColour = new Colours();

    colourOption = new SendableChooser<>();
    colourOption.addOption("Red", new Red());
    colourOption.addOption("Green", new Green());
    colourOption.addOption("Blue", new Blue());
    colourOption.addOption("Purple", new Purple());
    colourOption.addOption("Yellow", new Yellow());
    colourOption.addOption("Orange", new Orange());
    colourOption.addOption("White", new White());
    colourOption.addOption("Off", new Off());
    SmartDashboard.putData(colourOption);
  }

  public boolean busy()
  {
    if (buffer == null)
    {
      return false;
    }

    return index < buffer.length;
  }

  @Override
  public void periodic()
  {
    if (busy() && timer.get() >= TX_DELAY)
    {
      byte [] value = new byte[1];
      value[0] = (byte)buffer[index];
      index++;

      uart.write(value, 1);
      uart.flush();

      timer.reset();
    }
  }

  private void transmit(int [] data)
  {
    if (!busy() && data != null)
    {
      buffer = new int[data.length];
      index = 0;

      for (int i = 0; i < data.length; i++)
      {
        buffer[i] = data[i];
      }
    }
  }

  public void setColour(int red, int green, int blue)
  {
    int [] colourPacket = new int[4];

    colourPacket[0] = HEADER;
    colourPacket[1] = red;
    colourPacket[2] = green;
    colourPacket[3] = blue;

    transmit(colourPacket);
  }

  public void pickColour()
  {
    Colours colour = colourOption.getSelected();

    if (isSameColour(colour, oldColour))
    {
      return;
    }
    
    setColour(colour.red, colour.green, colour.blue);

    oldColour.red = colour.red;
    oldColour.green = colour.green;
    oldColour.blue = colour.blue;
  }

  public boolean isSameColour(Colours colour1, Colours colour2)
  {
    return (colour1.red == colour2.red) && (colour1.green == colour2.green) && (colour1.blue == colour2.blue);
  }

  // COLOURS //

  public void off()
  {
    setColour(0, 0, 0);
  }
}
