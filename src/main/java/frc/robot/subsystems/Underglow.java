/* ==================================================
 * Authors:
 * Shane Pinto
 * Kyle Pinto
 * --------------------------------------------------
 * Description: Code for LED Light strips underneath
 * robot. Communicates via Serial using the TTL UART
 * on the RoboRIO MXP port.  Bytes will only be
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

  private SerialThread serialThread;
  private int index;       // index of the next byte to be transmitted
  private int [] buffer;   // data to be transmitted
  private Object mutex;    // buffer mutex

  private SendableChooser<Colours> colourOption;
  private Colours oldColour;

  public Underglow()
  {
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

    buffer = null;
    index = 0;
    mutex = new Object();
    
    serialThread = new SerialThread();
    serialThread.start();
  }

  // Do not call this method without acquiring the mutex first
  private boolean busy()
  {
    if (buffer == null)
    {
      return false;
    }

    return index < buffer.length;
  }

  private class SerialThread extends Thread
  {
    private static final int BAUD_RATE = 9600;
    private static final double TIMEOUT = 100e-3;    // 100 ms
    private static final double TX_DELAY = 1.5e-3;   // 1.5 ms

    private SerialPort uart;
    private Timer timer;       // tracks the time between transmitted bytes

    public SerialThread()
    {
      uart = new SerialPort(BAUD_RATE, Port.kMXP, 8, Parity.kNone, StopBits.kOne);
      uart.reset();
      uart.setFlowControl(FlowControl.kNone);
      uart.setTimeout(TIMEOUT);

      timer = new Timer();
      timer.reset();
      timer.start();
    }

    @Override
    public void run()
    {
      boolean write = false;
      byte value = 0;

      while (true)
      {
        // acquire mutex and load next byte
        synchronized (mutex)
        {
          if (busy() && timer.get() >= TX_DELAY)
          {
            value = (byte)buffer[index];
            index++;
            write = true;
          }
        }

        // TX byte after releasing mutex.
        // write() may block so we release the mutex to unblock transmit().
        if (write)
        {
          byte [] writeBuffer = new byte[1];
          writeBuffer[0] = value;
          uart.write(writeBuffer, 1);
          uart.flush();
          timer.reset();
          write = false;
        }
      }
    }
  }

  private void transmit(int [] data)
  {
    // acquire the mutex and save the data if there isn't other data waiting to be transmitted
    synchronized (mutex)
    {
      if (busy() || data == null)
      {
        return;
      }

      buffer = new int[data.length];
      index = 0;

      for (int i = 0; i < data.length; i++)
      {
        buffer[i] = data[i];
      }
    }
  }

  // PUBLIC API //

  public void setColour(int red, int green, int blue)
  {
    int [] colourPacket = new int[4];

    colourPacket[0] = HEADER;
    colourPacket[1] = red;
    colourPacket[2] = green;
    colourPacket[3] = blue;

    transmit(colourPacket);
  }

  public void setAlignment(boolean aligned)
  {
    Colours colour;

    if (aligned)
    {
      colour = new Green();
    }
    else
    {
      colour = new Red();
    }

    if (isSameColour(colour, oldColour))
    {
      return;
    }

    setColour(colour.red, colour.green, colour.blue);

    oldColour.red = colour.red;
    oldColour.green = colour.green;
    oldColour.blue = colour.blue;
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

  public void off()
  {
    setColour(0, 0, 0);
  }
}
