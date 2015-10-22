package uicomponents;

import system.Controller;
import telemetrics.FlightLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * LoadFlight (JPanel)
 * @author Aran Long
 *
 * The LoadFlight class provides the user with an interface allowing the selection of a Plane and flight log name.
 * Then a flight can be loaded with the FlightLogger class, streaming data to the Sensors. Validation is used to
 * ensure that only one flight is loaded at once and flights are not interrupted.
 */
public class LoadFlight extends JPanel {
    private FontMetrics fm;
    public static boolean uiVisible = false;

    public static JTextField planeNameField; //accessed from search class
    private JButton loadButton;
    private Search search;
    private FlightLogger flightLogger;

    private int menuWidth = 250;
    private int menuMinHeight = 60, menuMaxHeight = 390;
    private int textLeftMargin = 10;

    /**
     * Adds MouseListener to allow vertical Minimisation and Maximisation of JPanel.
     * Adds Button listener to load button to handle loading of flights with FlightLogger object.
     */
    private void setupListeners(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getY() <= 60) {
                    uiVisible = !uiVisible;
                }
            }
        });

        //reading for save button pressed, if this occurs, then saving the new data using the telemetrics.FlightLogger class.
        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                System.out.println(search.selectedPlane + ", " + search.selectedFlight); //debug
                flightLogger.streamLog(search.selectedPlane, search.selectedFlight);
            }
        });
    }

    /**
     * Sets up UI defaults for Search object (Size, Location, Number of rows, Flight visibility)
     * Adds Search Object to LoadFlight JPanel.
     */
    private void setupSearch(){
        search = new Search();
        search.setLocation(0, 100);
        search.setRows(3);
        search.setFlightsVisible(true);

        this.add(search);
    }

    /**
     * Sets up UI defaults for load button (Bounds, Colors, Border, Font, Visibility, Text).
     * Adds load button to LoadFlight JPanel.
     */
    private void setupLoadButton(){
        loadButton = new JButton();
        loadButton.setBounds(20, 340, 215, 40);
        loadButton.setBackground(Ui.widgetForegroundGray);
        loadButton.setForeground(Ui.widgetForegroundGreen);
        loadButton.setBorder(BorderFactory.createEmptyBorder());
        loadButton.setFont(Ui.ftMid);
        loadButton.setText("Load");
        loadButton.setVisible(true);
        this.add(loadButton);
    }

    /**
     * Class constructor, Sets UI defaults for LoadFlight JPanel (Opacity, Size, Layout).
     * Calls setupSearch, setupLoadButton, setupListeners methods.
     * Creates instance of FlightLogger class to enable loading of flights.
     */
    public LoadFlight(){
        this.setOpaque(false);
        this.setSize(new Dimension(menuWidth,menuMinHeight));
        this.setLayout(null);

        setupSearch();
        setupLoadButton();
        setupListeners();

        flightLogger = new FlightLogger();
    }

    boolean resized = false;

    /**
     * Draws Titles, Backgrounds, Logos based on Minimisation state.
     * Draws full menu if LoadFlight is Maximised ie. Title, All text fields, Labels, Buttons.
     * Draws basic menu if LoadFlight is Minimised ie. Title.
     * @param g
     */
    public void paintComponent(Graphics g){
        String titleText = "Load Flight";
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
            g2.setColor(Ui.widgetForegroundGreen);
            g2.drawString("Flight Name", textLeftMargin, 90);
        }
        else if (resized){
            resized = false;
            this.setSize(new Dimension(menuWidth,menuMinHeight));
        }

        if(Controller.busy){
            loadButton.setText("Stop");
        }
        else loadButton.setText("Load");
        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0,0,menuWidth,menuMinHeight);
        g2.setColor(Ui.widgetForegroundGreen);
        g2.fillOval(textLeftMargin, textLeftMargin, 40, 40);
        g2.drawString(titleText, 90, menuMinHeight - (fm.getAscent()));
        g2.setColor(Ui.widgetForegroundGray);
        g2.fillRect(0, this.getHeight() - 1, this.getWidth(), 1);
    }
}