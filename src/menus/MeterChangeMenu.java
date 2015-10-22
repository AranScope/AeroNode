package menus;

import telemetrics.Sensor;
import telemetrics.Sensors;
import uicomponents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * MeterChangeMenu Class
 * @author Aran Long
 *
 * This is a JPopupMenu used to change the currently assigned Sensor object of the source component: either a Meter, LineGraph or BoxGraph object.
 * This also handles the deletion of the currently selected source component should this option be selected from the menu.
 */
public class MeterChangeMenu extends JPopupMenu{
    private Component source;
    private String sourceName;

    /**
     * Class constructor, sets up default UI configuration and assigns Sensors to MenuItems. Creates delete menu item.
     */
    public MeterChangeMenu(){
        this.setBorder(BorderFactory.createEmptyBorder());
        final ColorPicker colorPicker = new ColorPicker();
        this.add(colorPicker);
        colorPicker.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Color color = colorPicker.getColor();
                sourceName = source.toString();

                if(sourceName.contains("Meter")){
                    Meter sourceMeter = (Meter)source;
                    sourceMeter.setColor(color);
                }
                else if(sourceName.contains("LineGraph")){
                    LineGraph lineGraph = (LineGraph)source;
                    lineGraph.setColor(color);
                }
                else if(sourceName.contains("BoxGraph")){
                    BoxGraph boxGraph = (BoxGraph)source;
                    boxGraph.setColor(color);
                }
            }
        });

        addSensorMenuItems();
        addDeleteMenuItem();
    }

    /**
     * Sets global component variable to the component which is right clicked.
     * @param component
     */
    public void setSource(Component component){
        source = component;
    }

    /**
     * Configures graphics options of menuItem parameter (background, foreground, border, font)
     * @param menuItem
     * @return
     */
    private JMenuItem setMenuItemGraphics(JMenuItem menuItem){
        menuItem.setBackground(Ui.widgetForegroundGray);
        menuItem.setForeground(Ui.widgetForegroundGreen);
        menuItem.setBorderPainted(false);
        menuItem.setFont(Ui.ftSide);
        return menuItem;
    }

    /**
     * Adds actionListeners to menuItem objects, checks Data type of component object, assigns Sensors to component based on menuItem selection.
     * @param menuItem
     * @param sensor
     * @return
     */
    private JMenuItem addMenuItemListener(JMenuItem menuItem, final Sensor sensor){
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sourceName = source.toString();
                if(sensor == null){
                    source.getParent().remove(source); //deleting the Ui element as deleteItem in JPopupMenu has been selected
                }
                else if(sourceName.contains("Meter")){
                    Meter sourceMeter = (Meter)source;
                    sourceMeter.setSensor(sensor);
                }
                else if(sourceName.contains("LineGraph")){
                    LineGraph lineGraph = (LineGraph)source;
                    lineGraph.setSensor(sensor);
                }
                else if(sourceName.contains("BoxGraph")){
                    BoxGraph boxGraph = (BoxGraph)source;
                    boxGraph.setSensor(sensor);
                }
            }
        });

        return menuItem;
    }

    /**
     * Populates MeterChangeMenu with menu items from the Sensors.getSensorList Sensor array. Sets up graphics defaults, adds menu item listeners.
     */
    private void addSensorMenuItems(){
        for(final Sensor sensor: Sensors.getSensorList()){ //getting Sensor objects array from Sensors class.
            JMenuItem menuItem = new JMenuItem(sensor.getName()); //creating new menu item with name of Sensor.
            setMenuItemGraphics(menuItem); //calling function to setup graphics of menu item.
            addMenuItemListener(menuItem, sensor); //calling function to add mouse listener to menu item.
            add(menuItem); //adding menu item to JPopupMenu.
        }
    }

    /**
     * Adds menu item to allow deletion of component which has been right clicked.
     */
    private void addDeleteMenuItem(){
        JMenuItem deleteItem = new JMenuItem("Delete"); //creating delete menu item (used to delete source component).
        setMenuItemGraphics(deleteItem);
        deleteItem.setForeground(Ui.widgetForegroundRed);
        addMenuItemListener(deleteItem, null);
        add(deleteItem);
    }
}
