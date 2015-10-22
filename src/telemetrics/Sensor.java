package telemetrics;

/**
 * telemetrics.Sensor Class
 * @author Aran Long
 *
 * This is the telemetrics.Sensor Class, this is a template for telemetrics.Sensor objects. The class includes set and get methods for
 * 'name', 'value' and 'units'. These allow for access to telemetrics.Sensor data from any location that is within the scope
 * of a telemetrics.Sensor Object. Both 'name' and 'units' are parsed to the local values in the class constructor.
 */

public class Sensor {
    double value = 0;
    String name = "";
    String units = "";

    /**
     * Class constructor, sets name and units based on respective parameters.
     *
     * @param name
     * @param units
     */
    public Sensor(String name, String units){
        this.name = name;
        this.units = units;
    }

    //procedure to set the value of the current Sensor object, used by various classes.

    /**
     * Sets double value of Sensor object.
     *
     * @param parseValue
     */
    public void setValue(double parseValue){
        value = parseValue;
    }

    /**
     * Sets string name of Sensor object.
     * @param parseName
     */
    public void setName(String parseName){
        name = parseName;
    }

    /**
     * Sets string units of Sensor object.
     *
     * @param parseUnits
     */
    public void setUnits(String parseUnits){
        units = parseUnits;
    }

    /**
     * Returns double value of Sensor object.
     *
     * @return
     */
    public double getValue(){
        return value;
    }

    /**
     * Returns String name of Sensor object.
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Returns string units of Sensor object.
     *
     * @return
     */
    public String getUnits(){
        return units;
    }


}