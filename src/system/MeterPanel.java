package system;

import menus.NewMeterListener;
import telemetrics.Sensors;
import uicomponents.*;

import javax.swing.*;
import java.awt.*;

/**
 * Meter Panel Class
 * @author Aran Long
 *
 * This is a JPanel class containing all meter and graph objects, this also assigns telemetrics.Sensor objects from the telemetrics.Sensors class to these objects.
 */
public class MeterPanel extends JPanel {

    /**
     * Sets up MeterPanel UI defaults, Size, Layout etc. Adds NewMeterListener to allow right click menu to appear.
     */
    private void setupPanel(){
        this.setOpaque(false);
        this.setSize(2000, 2000);
        this.setPreferredSize(new Dimension(2000, 2000));
        this.setLayout(null);

        this.addMouseListener(new NewMeterListener());
    }

    //procedure to create all initially displayed UI Elements including the google maps StaticMap JPanel, Meter JPanel's as well as LineGraph and BoxGraph panels.
    //this also assigns Sensors and Colors to each UI element object, sets their location to an empty Ui.position grid position and adds them to the MeterPanel.

    /**
     * Adds Initial Meter JPanel objects to MeterPanel Including google maps StaticMap, LineGraph and BoxGraph objects.
     * Assigns Sensors and Colors to each JPanel object and location based on Ui.position grid position.
     */
    private void addInitialMeters(){
        StaticMap staticMap = new StaticMap();
        staticMap.setColor(Ui.randomColor());

        Meter meter = new Meter();
        meter.setSensor(Sensors.altimeter);
        meter.setMeterType(1);
        meter.setColor(Ui.randomColor());

        final LineGraph lineGraph = new LineGraph();
        lineGraph.setSensor(Sensors.altimeter);
        lineGraph.setColor(Ui.randomColor());

        final Meter speed = new Meter();
        speed.setSensor(Sensors.speed);
        speed.setColor(Ui.randomColor());

        final PlaneView planeView = new PlaneView();

        add(speed);
        add(lineGraph);
        add(meter);
        add(staticMap);
        add(planeView);

        planeView.setLocation(Ui.position[0]);
        //altimeter.setLocation(Ui.position[0]);
        speed.setLocation(Ui.position[2]);

        lineGraph.setLocation(Ui.position[8]);
        meter.setLocation(Ui.position[3]);
        staticMap.setLocation(Ui.position[6]);
    }

    //constructor to call setupPanel procedure (to configure JPanel settings) and addInitialMeters procedure to add the appropriate
    //Ui JPanel objects to the MeterPanel JPanel.

    /**
     * Class constructor, calls setupPanel, addInitialMeters methods.
     */
    public MeterPanel(){
        setupPanel();
        addInitialMeters();
    }
}