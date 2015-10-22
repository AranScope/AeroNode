package uicomponents;

import telemetrics.FlightLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;

/**
 * NewPlane (JPanel)
 * @author Aran Long
 *
 * The NewPlane class allows users to create new Plane sub-directories in the Planes folder.
 * It ensures that existnig Plane directories are not overwritten.
 */
public class NewPlane extends JPanel {
    private FontMetrics fm;
    public static boolean uiVisible = false;

    public static JTextField planeNameField;
    private JButton saveButton;
    private int textFieldLeftMargin = 10;
    private int textFieldWidth = 235, textFieldHeight = 40;
    private int menuWidth = 250;
    private int menuMinHeight = 60, menuMaxHeight = 200;
    private int textLeftMargin = 10;

    private Color foregroundColor = Ui.widgetForegroundGreen;

    /**
     * Adds MouseListener to allow vertical Minimisation and Maximisation of JPanel.
     * Adds Button listener to save button to handle saving of new planes with FlightLogger object.
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

        //reading for save button pressed, if this occurs, then saving the new data using the telemetrics.FlightLogger class.
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(planeNameField.getText().length() < 1){
                    System.out.println("No plane name entered");
                    //TopBar.setErrorMessage("No plane name entered");
                }
                else if(Arrays.asList(FlightLogger.getPlanes()).contains(planeNameField.getText())){
                    System.out.println("The plane already exists");
                }
                else{
                    try {
                        FlightLogger.createPlane(planeNameField.getText());
                    }catch(IOException ioe){System.out.println(e.toString());}
                    planeNameField.setText("");
                    Search.update();
                }
            }
        });
    }

    /**
     * Sets up UI defaults for Plane name field (Bounds, Colors, Border, Font, Visibility)
     * Adds Plane name field to NewPlane JPanel.
     */
    private void setupPlaneNameField(){
        //setting default bounds, colors, sizings, and visibility of all text fields.
        planeNameField = new JTextField();
        planeNameField.setBounds(textFieldLeftMargin, 100, textFieldWidth, textFieldHeight);
        planeNameField.setBackground(Ui.widgetForegroundGray);
        planeNameField.setForeground(foregroundColor);
        planeNameField.setBorder(BorderFactory.createEmptyBorder());
        planeNameField.setCaretColor(Color.decode("0xD0CED6"));
        planeNameField.setFont(Ui.ftMid);
        planeNameField.setVisible(true);

        this.add(planeNameField);
    }

    /**
     * Sets up UI defaults for save button (Bounds, Colors, Border, Font, Visibility, Text).
     * Adds save button to NewPlane JPanel.
     */
    private void setupSaveButton(){
        saveButton = new JButton();
        saveButton.setBounds(20, 150, 215, 40);
        saveButton.setBackground(Ui.widgetForegroundGray);
        saveButton.setForeground(foregroundColor);
        saveButton.setBorder(BorderFactory.createEmptyBorder());
        saveButton.setFont(Ui.ftMid);
        saveButton.setText("Save");
        saveButton.setVisible(true);

        this.add(saveButton);
    }

    /**
     * Class constructor, Sets UI defaults for NewPlane JPanel (Opacity, Size, Layout).
     * Calls setupPlaneNameField, setupSaveButton, setupListeners methods.
     */
    public NewPlane() {
        this.setOpaque(false);
        this.setSize(new Dimension(menuWidth, menuMinHeight));
        this.setLayout(null);

        setupPlaneNameField();
        setupSaveButton();
        setupListeners();
    }

    boolean resized = false;

    /**
     * Draws Titles, Backgrounds, Logos based on Minimisation state.
     * Draws full menu if NewPlane is Maximised ie. Title, All text fields, Labels, Buttons.
     * Draws basic menu if NewPlane is Minimised ie. Title.
     * @param g
     */
    public void paintComponent(Graphics g){
        String titleText = "New Plane";
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
        }
        else if (!uiVisible && resized){
            resized = false;
            this.setSize(new Dimension(menuWidth,menuMinHeight));
        }

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0,0,menuWidth,menuMinHeight);
        g2.setColor(foregroundColor);
        g2.fillOval(textLeftMargin, textLeftMargin, 40, 40);
        g2.drawString(titleText, 90, menuMinHeight - (fm.getAscent()));
        g2.setColor(Ui.widgetForegroundGray);
        g2.fillRect(0, this.getHeight() - 1, this.getWidth(), 1);
    }
}