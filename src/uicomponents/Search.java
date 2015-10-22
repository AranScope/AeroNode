package uicomponents;

import telemetrics.FlightLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Search Class (JPanel)
 * @author Aran Long
 *
 * The search class provides methods allowing the navigation of file trees. Specifically, through
 * the Plane folders and Flight folders.
 */
public class Search extends JPanel {
    private JTextField searchField = new JTextField();
    private String searchTerm = "";
    private static String[] flightData;
    private int menuHeight = 0;
    private int menuItemHeight = 60;
    private int menuItemMargin = 2;
    private static int listLength = 0;
    private int listWidth = 250, listHeight = 900;
    private int xmargin = 10, ymargin = 0;
    private int textFieldWidth = 235, textFieldHeight = 40;
    private int textFieldMargin = 5;
    private int textLeftMargin = 20, textTopMargin = 40;
    private boolean flightsVisible = false;

    String selectedFlight = "";
    String selectedPlane = "";

    private Color foregroundColor = Ui.widgetForegroundGreen;

    /**
     * Returns current height of the Search JPanel object.
     * @return
     */
    public int getHeight(){
        return listHeight;
    }

    /**
     * Sets whether flight names or just plane names should be visible in Search list.
     *
     * @param flightsVisible
     */
    public void setFlightsVisible(boolean flightsVisible){
        this.flightsVisible = flightsVisible;
    }

    /**
     * Sets the number of rows to appear in the Search list based on rows parameter.
     *
     * @param rows
     */
    public void setRows(int rows){
        listHeight = searchField.getHeight() + textFieldMargin + rows * (menuItemHeight + menuItemMargin);
        this.setSize(new Dimension(listWidth, listHeight));
    }

    /**
     * Adds Listeners to:
     * enable selection of flight name and/or plane name from Search list.
     * enable text entry and searching with search text field, updating and condensing of Search list values based on Search term.
     * enable scrolling up and down list to reveal following and previous search data returned from search.
     */
    private void setupListeners(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int y =0;
                super.mouseReleased(e);

                for(String flightDatum: flightData){
                    if(flightDatum.contains(searchTerm)){
                        if(e.getY() > menuHeight + searchField.getHeight() + textFieldMargin + (y * (menuItemHeight + menuItemMargin)) && e.getY() < menuHeight + searchField.getHeight() + textFieldMargin + (y * (menuItemHeight + menuItemMargin)) + menuItemHeight){
                            selectedFlight = flightDatum;//Possibly meant to use y here instead of x.
                            searchField.setText(flightDatum);
                        }
                        y++;
                    }
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                searchTerm = searchField.getText();
                update();
                menuHeight = 0; //reset list to 0 position when text entered
                repaint();
            }
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getUnitsToScroll() < 0 && menuHeight < 0){
                    menuHeight -= (menuItemHeight + menuItemMargin)* -1;
                }
                else if(e.getUnitsToScroll() > 0 && menuHeight > -(listLength -1)*(menuItemHeight + menuItemMargin)){
                    menuHeight -= menuItemHeight + menuItemMargin;
                }
            }
        });
    }

    /**
     * Sets up UI defaults for search field (Bounds, Colors, Border, Font, Visibility)
     * Adds Plane name field to Search JPanel.
     */
    private void setupSearchField(){
        searchField.setFont(Ui.ftMid);
        searchField.setBackground(Ui.widgetForegroundGray);
        searchField.setForeground(foregroundColor);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setCaretColor(Ui.caretColor);
        searchField.setBounds(xmargin, ymargin, textFieldWidth, textFieldHeight);
        this.add(searchField);
    }

    /**
     * Class constructor, Retrieves list of Planes from FlightLogger object.
     * Sets UI defaults (opacity, layout, size) of Search JPanel.
     * Calls setupSearchField, setupListeners methods.
     */
    public Search(){
        flightData = FlightLogger.getPlanes();
        listLength = flightData.length;

        this.setOpaque(false);
        this.setLayout(null);
        this.setSize(new Dimension(listWidth, listHeight));

        setupSearchField();
        setupListeners();
    }

    /**
     * Updates the currently displayed Search results with the FlightLogger object,
     * redetermines length of returned results to scale UI properly.
     */
    public static void update(){
        flightData = FlightLogger.getPlanes();
        listLength = flightData.length;
    }

    /**
     * Returns entered Search text from search text field.
     *
     * @return
     */
    public String getText(){
        return searchField.getText();
    }

    /**
     * Draws results of Search entry from FlightLogger data in a vertical list format.
     * Including Backgrounds, Result text, Foreground and Background Colors.
     * Draws Data on Flights when Plane is selected if FlightsVisible is true and the Plane has existing recorded flight data.
     *
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 0; //current row
        for (int x = 0; x < flightData.length; x++) {
            if (flightData[x].contains(searchTerm)) {
                g2.setColor(Ui.widgetForegroundGray);
                g2.fillRect(searchField.getX(), menuHeight + searchField.getHeight() + textFieldMargin + (y * (menuItemHeight + menuItemMargin)), searchField.getWidth(), menuItemHeight); //Drawing backgrounds for each individual Search result.
                g2.setFont(Ui.ftMid);

                String[] flights;
                if(flightsVisible && (flights = FlightLogger.getFlights(searchField.getText())) != null && searchField.getText().equals(flightData[x])){ //Checking whether Flights are allowed to be visible and Selected Plane has pre-recorded flights.
                    selectedPlane = flightData[x];
                    flightData = flights; //Setting displayed data to be flight names from plane sub-directory rather than Plane names from Planes directory.
                    menuHeight = 0; //reset list to 0 position when flight data modified
                    break;
                }

                g2.setColor(foregroundColor);
                g2.drawString(flightData[x], searchField.getX() + textLeftMargin, menuHeight + searchField.getHeight() + textTopMargin + (y * (menuItemHeight + menuItemMargin))); //Drawing text data from flight data array based on returned data from FlightLogger query.

                y++;
            }
        }
        listLength = y;

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(searchField.getX(), searchField.getHeight(), searchField.getWidth(), textFieldMargin); //Drawing background of Search JPanel.
    }
}