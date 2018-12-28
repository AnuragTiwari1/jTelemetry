import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        final SerialPort comPort = SerialPort.getCommPort(port);
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                String result = new String(newData);
                char[] chars=result.toCharArray();
                for (char ch:
                     chars) {
                    int ascii=ch;
                    if(ascii==10)
                        System.out.println();
                    else
                        System.out.print(ch);
                }
            }
        });
}
}
