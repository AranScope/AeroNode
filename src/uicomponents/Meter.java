package uicomponents;

import menus.MeterChangeListener;
import telemetrics.Sensor;

import javax.swing.*;
import java.awt.*;

/**
 * Meter Class (JPanel)
 * @author Aran Long
 *
 * This is the uicomponents.Meter JPanel class (Template for uicomponents.Meter objects). Each meter object will have an assigned sensor with the use
 * of the setSensor Method. These values are displayed within a paintComponent with the use of the telemetrics.Sensor's getValue() method.
 * Data is displayed using DrawString() methods and fontmetrics to allow for the string to be centred within the meter.
 */
public class Meter extends JPanel {
    private int value = 0;
    private int maxValue = value;
    private FontMetrics fm;
    private int titleX = 15, titleY = 45;
    private int unitsX = 210, unitsY = 225;
    private String meterName, meterUnits;
    private  Color foregroundColor = Ui.widgetForegroundBlue;

    private boolean isRoundMeter = false;

    Sensor sensor;

    public void setColor(Color foregroundColor){
        this.foregroundColor = foregroundColor;
    }

    /**
     * Adds MeterChangeListener, MouseDragListeners to Meter.
     */
    private void setupListeners(){
        this.addMouseListener(new MeterChangeListener());
        Ui.assignMouseListeners(this);
    }

    /**
     * Class constructor, Sets UI defaults (opacity, size) of Meter.
     * Calls setupListeners method.
     */
    public Meter(){
        this.setOpaque(false);
        this.setSize(new Dimension(Ui.meterWidth,Ui.meterHeight));

        setupListeners();
    }

    /**
     * Manually sets the String name of the Meter based on passed meterName string.
     *
     * @param meterName
     */
    public void setName(String meterName){
        this.meterName = meterName;
    }

    /**
     * Sets the type of the meter (Regular: 0, Round: 1) based on the passed meterType integer.
     * @param meterType
     */
    public void setMeterType(int meterType){
        if(meterType == 1){
            this.isRoundMeter = true;
        }
    }

    /**
     * Manually sets the String units of the Meter based on the passed meterUnits String.
     * @param meterUnits
     */
    public void setUnits(String meterUnits){
        this.meterUnits = meterUnits;
    }

    /**
     * Manually sets the Integer value of the Meter based on the passed value integer.
     * @param value
     */
    public void setValue(int value){
        this.value = value;
    }

    /**
     * Assigns Sensor to Meter object, determinant of data source, title and units of the Meter object.
     * @param sensor
     */
    public void setSensor(Sensor sensor){
        this.sensor = sensor;
        meterName = sensor.getName();
        meterUnits = sensor.getUnits();
    }

    /**
     * Draws value, title, units, UI accents, Arc (if round meter type) to JPanel.
     * If Meter type is round, Linearly interpolates angle of Arc such that the maximum angle is 180 degrees
     * and corresponds to the Max value from the input data.
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0, 0, Ui.meterWidth, Ui.meterHeight);

        g2.setColor(foregroundColor);
        g2.fillRect(0, 0, Ui.meterWidth, 10);
        if(sensor!=null) {
            value = (int) sensor.getValue();
        }
        else value = 0;
        if(value > maxValue) maxValue = value;

        g2.setFont(Ui.ftLrg);
        fm = g2.getFontMetrics();
        g2.drawString(String.valueOf(value), Ui.meterWidth/2 - (fm.stringWidth(String.valueOf(value))/2) - 2, 125 + (fm.getHeight()/3));

        g2.setFont(Ui.ftMid);
        g2.setColor(foregroundColor);
        g2.drawString(meterName, titleX, titleY);
        g2.drawString(meterUnits, unitsX, unitsY);

        value = (int)(value * (100.0/ maxValue));

        if(isRoundMeter) {
            g2.setColor(Ui.widgetForegroundGray);
            g2.setStroke(new BasicStroke(20.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            g2.drawArc(20, 50, 210, 210, 0, 180);
            g2.setColor(foregroundColor);
            int ans = (int) ((100 - value) * 1.8);
            g2.drawArc(20, 50, 210, 210, ans, 180 - ans);
        }
    }
}