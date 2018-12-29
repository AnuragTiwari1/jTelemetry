import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainApplication {
    public static void main(String[] args) throws IOException {
        String filePath= new String("rpiData.txt");
        File file = new File(filePath);
        if(!file.exists()){
            System.out.println("crated new file");
            file.createNewFile();
        }
        List<Character> charList = new ArrayList <>();
        SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        if(comPort.isOpen()){
            System.out.println("opened Port for communication::"+comPort.getSystemPortName()+"::curently talking on to :::"+comPort.getDescriptivePortName());
        }
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
                    if(ascii==10){
                        StringBuilder sb = new StringBuilder();

                        // Appends characters one by one
                        for (Character character : charList) {
                            sb.append(character);
                        }

                        // convert in string
                        String data = sb.toString();

                        // print string
                        System.out.println(data);
                        BufferedWriter output = null;
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                            writer.write(data);

                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        charList.add(ch);
                }
            }
        });
}
}
