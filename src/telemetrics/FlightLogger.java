package telemetrics;

import system.Controller;
import uicomponents.RecordFlight;

import java.io.*;
import java.util.*;

/**
 * Flight Logger Class
 * @author Aran Long
 *
 * This is a flight logger whose purpose is to read and write from the .dmlog flight log text files.
 * This will read data in from the .dmlog values and assign values to the associated sensors within the
 * Sensors class.
 */
public class FlightLogger {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private static String openTag = "<dmlog>";
    private static String closeTag = "</dmlog>";

    private static String log = "";
    private static int logLength = 0;
    private static int startIndex = 0;
    private static int endIndex = 0;

    private static Timer timer;

    private static String rootDirectory = System.getProperty("user.dir").replace("\\", "/");

    private int freq = 15;

    /**
     * Returns a list of existing flight log names within the planeName sub-directory.
     *
     * @param planeName
     * @return
     */
    public List<String> getLogNames(String planeName){
        List<String> results = new ArrayList<String>();
        File[] files = new File("Planes/" + planeName).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    //procedure to write a new log which will either overwrite or create a new file in the folder of the
    //plane name specified.

    /**
     * Writes a new flight log in the planeName flight directory with name 'flightName' and content 'flightLog'.
     *
     * @param planeName
     * @param flightName
     * @param flightLog
     * @throws IOException
     */
    public void writeLog(String planeName, String flightName, String flightLog) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(rootDirectory + "/Planes/" + planeName + "/" + flightName + ".dmlog"));
        bufferedWriter.write(flightLog);
        bufferedWriter.close();
    }

    //procedure to consistently encode and write inbound sensor data to dmlog files in the Planes respective sub-directory.

    /**
     * Periodically encodes and writes inbound sensor data to flight log file with name'flightName' in the 'planeName' sub-directory.
     *
     * @param planeName
     * @param flightName
     */
    public void streamWriteLog(String planeName, String flightName){
        try {

            bufferedWriter = new BufferedWriter(new FileWriter(rootDirectory + "/Planes/" + planeName + "/" + flightName + ".dmlog")); //setting BufferedWriter output sub-directory based on function parameters.
            System.out.println("Recording has started");
            timer = new Timer(); //creating Timer object for TimerTask execution

            //task to write next flight log line to text file and ensure the task should continue.
            TimerTask write = new TimerTask() {
                @Override
                public void run() {
                    try {
                        bufferedWriter.write(openTag); //writing openTag to start of log line
                        bufferedWriter.write(Sensors.latitude.value + "," + Sensors.longitude.value + "," + Sensors.pitch.value + "," + Sensors.roll.value + "," + Sensors.heading.value + "," + Sensors.temp.value + "," +
                                Sensors.barometer.value + "," + Sensors.altimeter.value); //writing relevant sensor values between open and close tag in flight log
                        bufferedWriter.write(closeTag + "\n"); //writing closeTag and line break to end of log line


                        if(!RecordFlight.recording){ //checking if flight should still be recorded based on record button state in RecordFlight class
                            timer.cancel();
                            bufferedWriter.close();
                            System.out.println("Recording has stopped");
                        }
                    }catch (IOException ex){ex.printStackTrace();}
                }
            };

            timer.schedule(write, 0, 1000 / freq); //setting task to write TimerTask, delay to 0ms and timer period to 1000/frequency

        }catch(IOException ex){System.out.println("Exception: " + ex.toString());}
    }

    /**
     * Sets frequency of reading and writing flight log files.
     *
     * @param frequency
     */
    public void setFrequency(int frequency){
        freq = frequency;
    }

    /**
     * Reads 'flightName' flight log from 'planeName' sub-directory and returns string containing this flight log.
     *
     * @param planeName
     * @param flightName
     * @return
     * @throws IOException
     */
    public String readLog(String planeName, String flightName) throws IOException{
        String log = "", line;
        bufferedReader = new BufferedReader(new FileReader(rootDirectory + "/Planes/" + planeName + "/" + flightName + ".dmlog"));
        while((line = bufferedReader.readLine()) != null) log+=line;
        bufferedReader.close();
        return log;
    }

    /**
     * Periodically reads flight log data strings from 'flightName' flight log in 'planeName' sub-directory.
     * Assigns values to Sensor objects in Sensors class based parsed values from flight log data strings.
     *
     * @param planeName
     * @param flightName
     */
    public void streamLog(String planeName, String flightName){
        if(Controller.busy && timer != null){
            timer.cancel();
            Controller.busy = false;
            Sensors.setAll(0);
            return;
        }
        Controller.busy = true;
        timer = new Timer();
        log = "";
        String line;
        logLength = 0;
        startIndex = 0;
        endIndex = 0;

        try {
            bufferedReader = new BufferedReader(new FileReader(rootDirectory + "/Planes/" + planeName + "/" + flightName + ".dmlog")); //setting BufferedReader input sub-directory based on function parameters.
            while ((line = bufferedReader.readLine()) != null) { //reading in entire flight log to line string variable.
                log += line;
                logLength++;
            }
            bufferedReader.close();
        }catch(Exception e){System.out.println(e.toString());}

        //task to read and decode next flight log data line should there still be remaining, unread lines.
        TimerTask read = new TimerTask() {
            @Override
            public void run() {
                if(logLength > 0) { //checking if log has been fully read
                    startIndex = log.indexOf(openTag, endIndex) + openTag.length(); //locating start of comma separated sensor values.
                    endIndex = log.indexOf(closeTag, startIndex); //locating end of comma separated sensor values.
                    String currentLog = log.substring(startIndex, endIndex); //reading string of comma separated sensor values.
                    String[] sensorValues = currentLog.split(","); //splitting currentLog string based on comma ',' delimiter.

                    //setting all sensor values based on their respective position in the sensorValues array
                    Sensors.latitude.value = Double.parseDouble(sensorValues[0]);
                    Sensors.longitude.value = Double.parseDouble(sensorValues[1]);
                    Sensors.pitch.value = Double.parseDouble(sensorValues[2]);
                    Sensors.roll.value = Double.parseDouble(sensorValues[3]);
                    Sensors.heading.value = Double.parseDouble(sensorValues[4]);
                    Sensors.speed.value = Double.parseDouble(sensorValues[5]);
                    Sensors.temp.value = Double.parseDouble(sensorValues[6]);
                    Sensors.barometer.value = Double.parseDouble(sensorValues[7]);
                    Sensors.altimeter.value = Double.parseDouble(sensorValues[8]);
                    logLength--;

                    //if(logLength%20 == 0) {
                    //    StaticMap.longitude += 0.01;
                    //    StaticMap.loadMap(StaticMap.latitude, StaticMap.longitude);
                    //}

                }
                else{
                    timer.cancel(); //cancelling log read TimerTask execution by extension.
                    System.out.println("Reading has stopped");
                    Controller.busy = false; //setting the controller to be available again.
                    log = "";
                    Sensors.setAll(0); //assigning 0 to all sensor values.
                    Controller.busy = false;
                }
            }
        };

        timer.scheduleAtFixedRate(read, 0, 1000 / freq); //scheduling read TimerTask with delay of 0ms and time period of 1000/ frequency.
    }

    /**
     * Removes flight log of name 'flightName' from 'planeName' sub-directory.
     *
     * @param planeName
     * @param flightName
     * @return
     */
    public boolean removeLog(String planeName, String flightName){
        File file = new File(rootDirectory + "/Planes/" + planeName + "/" + flightName + ".dmlog");
        return file.delete();
    }

    //procedure to create a new plane sub directory for storing flight logs relating to this plane.

    /**
     * Creates new plane sub-directory of name 'planeName' in Planes directory.
     *
     * @param planeName
     * @throws IOException
     */
    public static void createPlane(String planeName) throws IOException{
        File file = new File(rootDirectory + "/Planes/" + planeName);
        file.mkdirs(); //creating plane sub-directory.
        file.createNewFile(); //creating plane file.
    }

    /**
     * Returns String array of all Plane folders within the Planes directory.
     * @return
     */
    public static String[] getPlanes(){
        File[] files = new File(rootDirectory + "/Planes").listFiles(); //retrieving array of daughter File objects from Planes sub-directory.
        String[] planeNames;
        if(files!=null) {
            planeNames = new String[files.length];

            for (int x = 0; x < files.length; x++) {
                planeNames[x] = files[x].getName(); //getting file name of plane File object.
            }

        }
        else planeNames = new String[0];

        return planeNames;
    }

    /**
     * Returns String array of all flight log file names within the 'planeName' sub-directory.
     * @param planeName
     * @return
     */
    public static String[] getFlights(String planeName){
        if(planeName.length() > 1) { //ensuring that planeName is not blank or null
            try {
                File[] files = new File(rootDirectory + "/Planes/" + planeName).listFiles(); //retrieving array of daughter file objects from planeName sub-directory.
                if (files!=null && files.length > 0) {
                    String[] flightNames = new String[files.length]; //getting file name of flightName File object.
                    for (int x = 0; x < files.length; x++) {
                        flightNames[x] = files[x].getName().substring(0, files[x].getName().length() - 6); //removing .dmlog file extension from end of file name.
                    }
                    return flightNames;
                }
            } catch(Exception ex){
                ex.printStackTrace();
                return null;}
        }
        return null;
    }

    /**
     * Checks whether flight log of name 'flightName' exists in the 'planeName' sub-directory.
     * Returns true if flight log exists.
     *
     * @param planeName
     * @param flightName
     * @return
     */
    public boolean flightExists(String planeName,String flightName){
        File[] files = null;
        try {
            files = new File(rootDirectory + "/Planes/" + planeName).listFiles(); //retrieving array of daughter file objects from planeName sub-directory.
        } catch(Exception ex){ex.printStackTrace();}

        if (files != null && files.length > 0) {
            int count = 0;
            for(File file: files){
                if(file.getName().equalsIgnoreCase(flightName)) return true;//checking if flight name parameter is equal to file name of flightName object.
                count++;
            }
        }

        return false;
    }

    /**
     * Checks whether a Plane of name 'planeName' exists in the Planes folder.
     * @param planeName
     * @return
     */
    public boolean planeExists(String planeName){
        File[] files = new File(rootDirectory + "/Planes").listFiles(); //retrieving array of daughter File objects from Planes sub-directory.
        String[] planeNames = new String[files.length];

        for(int x = 0; x< files.length; x++) {
            planeNames[x] = files[x].getName(); //getting file name of plane File object.
            if(planeNames[x].equals(planeName)) return true; //checking if plane name parameter is equal to file name of planeName object.
        }
        return false;
    }
}