package uicomponents;

import telemetrics.FlightLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * RecordFlight (JPanel)
 * @author Aran Long
 *
 * RecordFlight handles the selection of a Plane and flight name with
 * the Search class. It then enables users to record their flight data in
 * real time with the FlightLogger class and data validation to ensure multiple
 * flights cna not be recorded at the same time.
 */
public class RecordFlight extends JPanel {
    private FontMetrics fm;
    public static boolean uiVisible = false;
    public static boolean recording = false;

    private JTextField flightNameField;
    private JButton recordButton;
    private int textFieldLeftMargin = 10;
    private int textFieldWidth = 235, textFieldHeight = 40;
    private int menuWidth = 250;
    private int menuMinHeight = 60, menuMaxHeight = 470;
    private int textLeftMargin = 10;

    FlightLogger flightLogger;
    Search search;

    private Color foregroundColor = Ui.widgetForegroundGreen;

    /**
     * Adds MouseListener to allow vertical Minimisation and Maximisation of JPanel.
     * Adds Button listener to enable recording of flights with FlightLogger Object based on current recording state.
     */
    private void setupListeners(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getY() <= 60){
                    uiVisible = !uiVisible;
                }
            }
        });

        //reading for save button pressed, if this occurs, then saving the new data using the FlightLogger class.
        recordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!recording) {
                    if(!flightLogger.flightExists(search.selectedFlight, flightNameField.getText()) && flightNameField.getText().length() > 0 && flightLogger.planeExists(search.selectedFlight)) { //fix this
                        System.out.println(search.selectedFlight);
                        recording = true;
                        flightLogger.streamWriteLog(search.selectedFlight, flightNameField.getText());
                        search.update();
                    }
                }
                else{
                    recording = false;
                }
            }
        });
    }

    /**
     * Sets up UI defaults for Flight name field (Bounds, Colors, Border, Font, Visibility)
     * Adds Flight name field to RecordFlight JPanel.
     */
    private void setupFlightNameField(){
        //setting default bounds, colors, sizings, and visibility of all text fields.
        flightNameField = new JTextField();
        flightNameField.setBounds(textFieldLeftMargin, 370, textFieldWidth, textFieldHeight);
        flightNameField.setBackground(Ui.widgetForegroundGray);
        flightNameField.setForeground(foregroundColor);
        flightNameField.setBorder(BorderFactory.createEmptyBorder());
        flightNameField.setCaretColor(Color.decode("0xD0CED6"));
        flightNameField.setFont(Ui.ftMid);
        flightNameField.setVisible(true);

        this.add(flightNameField);
    }

    /**
     * Sets up UI defaults for record button (Bounds, Colors, Border, Font, Visibility, Text).
     * Adds record button to RecordFlight JPanel.
     */
    private void setupRecordButton(){
        recordButton = new JButton();
        recordButton.setBounds(20, 420, 215, 40);
        recordButton.setBackground(Ui.widgetForegroundGray);
        recordButton.setForeground(foregroundColor);
        recordButton.setBorder(BorderFactory.createEmptyBorder());
        recordButton.setFont(Ui.ftMid);
        recordButton.setText("Record");
        recordButton.setVisible(true);

        this.add(recordButton);
    }

    /**
     * Sets up UI defaults for Search object (Size, Location, Number of rows, Flight visibility)
     * Adds Search Object to RecordFlight JPanel.
     */
    private void setupSearch(){
        search = new Search();
        search.setLocation(0, 100);
        search.setRows(3);
        search.setFlightsVisible(false);
        this.add(search);
    }

    /**
     * Class constructor, Sets UI defaults for RecordFlight JPanel (Opacity, Size, Layout).
     * Calls setupSearch, setupRecordButton, setupFlightNameField, setupListeners methods.
     * Creates instance of FlightLogger class to enable recording of flights.
     */
    public RecordFlight(){
        this.setOpaque(false);
        this.setSize(new Dimension(menuWidth,menuMinHeight));
        this.setLayout(null);

        flightLogger = new FlightLogger();

        setupFlightNameField();
        setupRecordButton();
        setupSearch();
        setupListeners();
    }

    /**
     * Sets current recording state of RecordFlight object based on passed recording boolean.
     * @param recording
     */
    public void setRecording(Boolean recording){
        this.recording = recording;
    }

    /**
     * Sets Color of foreground elements (Text, Button text, Graphics shapes) of Flight recorder.
     * @param foregroundColor
     */
    public void setColor(Color foregroundColor){
        this.foregroundColor = foregroundColor;
    }

    boolean resized = false;

    /**
     * Draws Titles, Backgrounds, Logos based on Minimisation state.
     * Draws full menu if RecordFlight is Maximised ie. Title, All text fields, Labels, Buttons.
     * Draws basic menu if RecordFlight is Minimised ie. Title.
     * @param g
     */
    public void paintComponent(Graphics g){
        String titleText = "Record Flight";
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(Ui.ftMid);
        fm = g.getFontMetrics();

        //if this select plane interface is maximised, sets locations and draws titles of each
        //text field.
        if(uiVisible){
            if(!resized){
                this.setSize(new Dimension(menuWidth, menuMaxHeight));
                resized = true;
            }
            g2.setColor(Ui.widgetBackground);
            g2.fillRect(0, 0, menuWidth, menuMaxHeight);
            g2.setColor(foregroundColor);
            g2.drawString("Plane Name", textLeftMargin, 90);
            g2.drawString("Flight Name", textLeftMargin, 360);
        }
        else if (!uiVisible && resized){
            resized = false;
            this.setSize(new Dimension(menuWidth,menuMinHeight));
        }

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0,0,menuWidth,menuMinHeight);
        if(recording) g2.setColor(Ui.widgetForegroundRed);
        else g2.setColor(foregroundColor);

        g2.fillOval(textLeftMargin, textLeftMargin, 40, 40);
        g2.drawString(titleText, 90, menuMinHeight - (fm.getAscent()));
        g2.setColor(Ui.widgetForegroundGray);
        g2.fillRect(0, this.getHeight() - 1, this.getWidth(), 1);
    }
}