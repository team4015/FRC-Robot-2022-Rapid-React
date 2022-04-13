/* ==================================================
 * Authors: Shane Pinto
 *
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

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
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

  public Underglow()
  {
    uart = new SerialPort(BAUD_RATE, SerialPort.Port.kOnboard);
    uart.setTimeout(TIMEOUT);

    timer = new Timer();
    timer.reset();
    timer.start();

    buffer = null;
    index = 0;
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

  // COLOURS //

  public void off()
  {
    setColour(0, 0, 0);
  }

  public void blue()
  {
    setColour(0, 0, 255);
  }

  public void red()
  {
    setColour(255, 0, 0);
  }

  public void green()
  {
    setColour(255, 0, 0);
  }

  public void purple()
  {
    setColour(200, 0, 128);
  }

  public void yellow()
  {
    setColour(125, 255, 0);
  }

  public void orange()
  {
    setColour(240, 255, 0);
  }

  public void white()
  {
    setColour(100, 255, 255);
  }
}
