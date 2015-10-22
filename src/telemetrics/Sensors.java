package telemetrics;
/**
 * Sensors Class
 * @author Aran Long
 *
 * This is the telemetrics.Sensors Class, this is a set of pre-defined Sensor objects that are included in
 * the default Ui setup of AeroNode. This also contains methods for retrieving all existing sensors in an array and setting
 * the value of every Sensor object.
 */
public class Sensors {
    //all sensors included within aircraft, these are accessed from all graphical classes in the form of
    //setSensor(telemetrics.Sensor sensor) and the associated methods within the telemetrics.Sensor class.
    public static Sensor latitude = new Sensor("Latitude", "°D");
    public static Sensor longitude = new Sensor("Longitude", "°D");

    public static Sensor pitch = new Sensor("Pitch", "°D");
    public static Sensor roll = new Sensor("Roll", "°D");
    public static Sensor heading = new Sensor("Heading", "°D");

    public static Sensor speed = new Sensor("Speed", "m/s");

    public static Sensor temp = new Sensor("Temp", "°C");
    public static Sensor barometer = new Sensor("Pressure", "Pa");
    public static Sensor altimeter = new Sensor("Altitude", "m");

    //public static Sensor speedometer = new Sensor("Speed", "m/s");


    private static Sensor[] sensorList = {latitude, longitude, pitch, roll, heading, speed, temp, barometer, altimeter};

    /**
     * Returns Sensor array of default Sensor objects.
     */
    public static Sensor[] getSensorList(){
        return sensorList;
    }

    /**
     * Sets double value to all default Sensor objects.
     * @param value
     */
    public static void setAll(double value){
       for (Sensor sensor : sensorList) {
           sensor.setValue(value);
       }
    }
}