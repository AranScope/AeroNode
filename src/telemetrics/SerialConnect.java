package telemetrics;

import jssc.*;
import system.Controller;

/**
 * Serial Connect Class
 *
 * @author Aran Long
 *         <p/>
 *         This is the Serial Connect Class. This handles communication between the micro-controller and the program itself. A Serial Connection
 *         Library named 'JSSC' or 'Java Simple Serial Connector' is used to handle the decoding of incoming serial data to String objects. This
 *         class also handles the selection of the appropriate serial port that the micro-controller is connected to, assumed to be the most recently
 *         connected device and hence, the serial port with the highest index (COMINDEX).
 */
public class SerialConnect {
    static StringBuilder message;

    /**
     * Returns the highest value serial port, i.e. Most recently connected serial device (Micro-Controller).
     *
     * @return
     */
    private String getCurrentPort() {
        String[] portNames = SerialPortList.getPortNames();
        return portNames[portNames.length - 1];
    }

    /**
     * Sets up and opens serial port based on parameters.
     *
     * @param port     Serial port identifier
     * @param baudRate Polling rate for serial port
     * @param dataBits Data bits in serial buffer
     * @param stopBits Stop bits in serial buffer
     * @param parity   Parity for serial buffer 0: no parity, 1: even parity, 2: odd parity
     * @return
     */
    private SerialPort setupSerialPort(String port, int baudRate, int dataBits, int stopBits, int parity) {
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, dataBits, stopBits, parity);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return serialPort;
    }

    /**
     * Adds serial port listener to serial port.
     * Continually polls serial buffer for new data, when data is received this decodes the data, assigning values
     * to sensors and printing the buffer to the serial monitor.
     *
     * @param serialPort
     */
    private void addSerialPortListener(final SerialPort serialPort) {
        message = new StringBuilder("");

        try {
            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent serialPortEvent) {
                    try {
                        if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0 && !Controller.busy) {
                            byte buffer[] = serialPort.readBytes(); //read current data from serial port.
                            for (byte b : buffer) {
                                if (((b == '\r' || b == '\n') && message.length() > 0)) { //if delimiter found at the end of message.

                                    decodeAndDisplay(message.toString().replace("\n", "")); //remove new line delimeters from data string, split message by Comma "," and then convert values to doubles and assign to Sensor objects.
                                    message.setLength(0); //reset current message.

                                } else if (((char) b >= 48 && (char) b <= 57) || ((char) b >= 44 && (char) b <= 46)) { //appending numbers 0-9 ascii, comma ascii, decimal point ascii, minus sign ascii to the current data string.
                                    message.append((char) b); //append character to current message.
                                }
                            }
                        }
                    } catch (SerialPortException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Class constructor, gets and configures current port, adds serial data listener to port.
     */
    public SerialConnect() {
        String port = getCurrentPort();
        SerialPort serialPort = setupSerialPort(port, 9600, 8, 1, 0);
        addSerialPortListener(serialPort);
    }

    /**
     * Decodes serial data, ensuring data is valid, assigning sensor values from decoded data.
     *
     * @param currentLog
     */
    private void decodeAndDisplay(String currentLog) {
        if (currentLog.length() > 0) {
            if (currentLog.matches("(-?[0-9]+.?[0-9]+,?)+")) {
                String[] sensorValues = currentLog.split(",");
                if (sensorValues.length == 8) {
                    System.out.println(currentLog);
                    Sensors.latitude.value = Double.parseDouble(sensorValues[0]);
                    Sensors.longitude.value = Double.parseDouble(sensorValues[1]);
                    Sensors.pitch.value = Double.parseDouble(sensorValues[2]);
                    Sensors.roll.value = Double.parseDouble(sensorValues[3]);
                    Sensors.heading.value = Double.parseDouble(sensorValues[4]);
                    Sensors.temp.value = Double.parseDouble(sensorValues[5]);
                    Sensors.barometer.value = Double.parseDouble(sensorValues[6]);
                    Sensors.altimeter.value = Double.parseDouble(sensorValues[7]);
                } else
                    System.out.println("SerialConnect: Current flight log length :" + sensorValues.length + ", Expected value: 8");
            } else System.out.println("SerialConnect: Current flight log does not match standard structure");
        } else System.out.println("SerialConnect: Current flight log is empty");

    }
}