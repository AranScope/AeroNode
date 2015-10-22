package system;

import telemetrics.Sensors;
import telemetrics.SerialConnect;
import uicomponents.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * system.Controller Class
 * @author Aran Long
 *
 * This is the main controller, this creates the main JFrame and creates instances of all the required JPanel objects including
 * uicomponents.Meter, uicomponents.TopBar etc.
 * This also creates an instance of the telemetrics.SerialConnect object to decode and display live serial data.
 */
public class Controller {
    private static JFrame frame = new JFrame("AeroNode");
    private int frameWidth = 1305, frameHeight = 830;
    public static boolean busy = false;
    private static MeterPanel meterPanel;

    //constructor used to create a SerialConnect object (used to read and decode serial data live) as well as call the setupFrame and setupMeters UI setup
    //methods and call the program loop.

    /**
     * Class constructor, creates SerialConnect object to read and decode serial data, sets up UI defaults, calls main program loop.
     */
    public Controller(){
        SerialConnect sc = new SerialConnect();
        setupFrame();
        setupMeters();
        programLoop();
    }

    //sets up default frame configurations including close operations, sizing, backgrounds and layouts.

    /**
     * Sets up frame UI defaults, loads frame icon image.
     */
    private void setupFrame(){
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
        frame.getContentPane().setBackground(Ui.frameBackground);
        frame.setLayout(null);

        frame.setUndecorated(true);
        frame.setVisible(true);

        Ui.screenSize = new Dimension(frameWidth, frameHeight);

        try{
            frame.setIconImage(ImageIO.read(new File("res/aeroicon.png")));
        }catch(Exception e){e.printStackTrace();}
    }

    /**
     * Adds a new Meter JPanel object to the MeterPanel, sets meter type base on meterType parameter, sets location of meter.
     * @param x
     * @param y
     * @param meterType RoundMeter: 1, Meter: 0
     */
    public static void addBlankMeter(int x, int y, int meterType) {
        Meter meter = new Meter();
        meter.setMeterType(meterType);
        meter.setSensor(Sensors.altimeter);
        meterPanel.add(meter);
        meter.setLocation(x, y);
    }

    /**
     * Adds a new LineGraph JPanel object to the MeterPanel, sets location of LineGraph.
     * @param x
     * @param y
     */
    public static void addBlankLineGraph(int x, int y){
        LineGraph lineGraph = new LineGraph();
        lineGraph.setSensor(Sensors.barometer);
        meterPanel.add(lineGraph);
        lineGraph.setLocation(x, y);
    }

    /**
     * Adds a new BoxGraph JPanel object to the MeterPanel, sets location of BoxGraph.
     * @param x
     * @param y
     */
    public static void addBlankBoxGraph(int x, int y){
        BoxGraph boxGraph = new BoxGraph();
        boxGraph.setSensor(Sensors.latitude);
        meterPanel.add(boxGraph);
        boxGraph.setLocation(x, y);
    }

    /**
     * Sets up TopBar, SideBar, MeterPanel JPanel objects, adds objects to frame.
     */
    private void setupMeters(){
        TopBar topBar= new TopBar(frame);
        final SideBar sideBar = new SideBar();
        meterPanel = new MeterPanel();
        meterPanel.setLocation(sideBar.getWidth(), topBar.getHeight());

        frame.add(topBar);
        frame.add(sideBar);
        frame.add(meterPanel);
        frame.pack();
    }


    private boolean loop = true;

    /**
     * Main program loop, repaints the program every 10ms to ensure visual data is valid and synchronized.
     */
    private void programLoop(){
        while (loop){
            try{
                Thread.sleep(10);
            }catch(Exception e){
                e.printStackTrace();
            }
            //System.out.println(FlightLogger.rootDirectory);
            frame.repaint();
            //PlaneView.yRotation += 0.1;
            //PlaneView.zRotation += 0.1;


            //counter+= 0.1;
            //float r = 4;
            //PlaneView.centrex = (float)(r * Math.sin(counter));
            //PlaneView.centrey = (float)(r * Math.cos(counter));

            //PlaneView.zRotation += 3;
            //PlaneView.yRotation += 2;
            //PlaneView.xRotation += 1;
            PlaneView.xRotation = (float)Sensors.pitch.getValue();
            //PlaneView.yRotation = (float)Sensors.heading.getValue();
            PlaneView.zRotation = -(float)Sensors.roll.getValue();
            PlaneView.glcanvas.repaint();
        }
    }
}