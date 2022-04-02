/* ==================================================
 * Authors: Shane Pinto
 *
 * --------------------------------------------------
 * Description: Code for LED Light strips underneath
 * robot. Communicates via Serial using the RS-232
 * port on the Roborio.
 * 
 * RGB Controller Repository: https://github.com/team4015/RGB-Controller
 * ================================================== */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;

public class Underglow extends SubsystemBase
{
  SerialPort uart;
  
  final int HEADER_BYTE = 0xFF;
  final int BAUD_RATE = 9600;
  final double TIMEOUT = 0.1; // 100 ms
  final double TRANSMIT_DELAY = 0.01; // 10 ms

  public Underglow()
  {
    uart = new SerialPort(BAUD_RATE, Port.kOnboard);

    uart.setTimeout(TIMEOUT);
  }

  public void transmitByte(int value)
  {
    byte [] buffer = {(byte) value};
    uart.write(buffer, buffer.length);
    uart.flush();
    Timer.delay(TRANSMIT_DELAY);
  }

  public void transmit(int [] data)
  {
    for (int i = 0; i < data.length; i++)
    {
      if (data[i] > 255 || data[i] < 0)
      {
        return;
      }
    }

    for (int i = 0; i < data.length; i++)
    {
      transmitByte(data[i]);
    }
  }

  public void setColour(int red, int green, int blue)
  {
    int [] colourPacket = {HEADER_BYTE, red, green, blue};
    transmit(colourPacket);
  }

  // COLOURS

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
    setColour(70, 0, 255);
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
