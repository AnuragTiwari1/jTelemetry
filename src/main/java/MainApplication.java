import com.fazecast.jSerialComm.SerialPort;

import java.io.InputStream;
import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args){

        SerialPort[] openPorts = SerialPort.getCommPorts();
        for (SerialPort s:
             openPorts) {
            System.out.println("open port>>>>"+s.getSystemPortName()+" serving>>>"+s.getDescriptivePortName());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("enter serial port to open (e.g., /dev/ttyS0 or COM3)::");
        String port = scanner.nextLine();
        SerialPort comPort = SerialPort.getCommPort(port);
        comPort.openPort();
        try {
            while (true)
            {
                while (comPort.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                System.out.println("arduino says>>>>>"+new String(readBuffer));
            }
        } catch (Exception e) { e.printStackTrace(); }
        comPort.closePort();
    }
}
