package uicomponents;

import javax.swing.*;
import java.awt.*;


/**
 * Side Bar Class (JPanel)
 * @author Aran Long
 *
 * This is the Side Bar JPanel Class. This is a JPanel object used to store daughter JPanel objects at the side
 * of the parent JFrame. This includes a minimising button that will hide the side bar and all daughter objects
 * allowing for any graphics objects behind the Side Bar to be made visible.
 *
 * A Select Plane JPanel Object is instantiated within the class as this is the main JPanel daughter object to be
 * displayed within the sidebar.
 */
public class SideBar extends JPanel {
    private static int sideBarWidth = 255, sideBarHeight = 2000;
    private static int sideBarTopMargin = 40;

    private NewPlane newPlane = new NewPlane();
    private LoadFlight loadFlight = new LoadFlight();
    private RecordFlight recordFlight = new RecordFlight();

    /**
     * Class constructor, Sets UI defaults (Opacity, Size, Layout) of SideBar JPanel.
     * Sets Position of NewPlane, LoadFlight and RecordFlight JPanel objects and adds these to SideBar JPanel.
     */
    public SideBar(){
        this.setOpaque(false);
        this.setSize(new Dimension(sideBarWidth, sideBarHeight));
        this.setLayout(null);

        newPlane.setLocation(0, 40);
        this.add(newPlane);

        //loadFlight.setLocation(0, newPlane.getLocation().y + newPlane.getHeight());
        loadFlight.setLocation(0, newPlane.getLocation().y + newPlane.getHeight());
        this.add(loadFlight);

        recordFlight.setLocation(0, loadFlight.getLocation().y + loadFlight.getHeight());
        this.add(recordFlight);
    }

    //Paint component for all non swing graphical elements including text, boxes, backgrounds etc.
    //added to JPanel at runtime. Draws background of sidebar if visibility is toggled to on state.

    /**
     * Draws Background of SideBar JPanel.
     * Repositions LoadFlight and RecordFlight JPanels based on position of vertical neighbours (ie. If above neighbour is maximised, then move down).
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0, sideBarTopMargin, sideBarWidth, sideBarHeight);

        loadFlight.setLocation(0, newPlane.getLocation().y + newPlane.getHeight());
        recordFlight.setLocation(0, loadFlight.getLocation().y + loadFlight.getHeight());

    }
}