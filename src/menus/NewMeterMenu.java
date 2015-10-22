package menus;

import system.Controller;
import uicomponents.Ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * NewMeterMenu Class
 * @author Aran Long
 *
 * This is a JPopupMenu used to create new Ui objects (Meters, LineGraphs and BoxGraphs) based on the three user selectable
 * option.
 */
public class NewMeterMenu extends JPopupMenu {
    private int xMeterPosition;
    private int yMeterPosition;

    /**
     * Configures graphics options of menuItem parameter (background, foreground, border, font)
     * @param menuItem
     * @return
     */
    private JMenuItem setupMenuItemGraphics(JMenuItem menuItem){
        menuItem.setBackground(Ui.widgetForegroundGray);
        menuItem.setForeground(Ui.widgetForegroundGreen);
        menuItem.setBorderPainted(false);
        menuItem.setFont(Ui.ftSide);
        return menuItem;
    }

    /**
     * Populates NewMeterMenu with menu items representing Meters, LineGraphs and BoxGraphs.
     */
    private void populateMenu(){
        JMenuItem newMeter;
        JMenuItem newRoundMeter;
        JMenuItem newLineGraph;
        JMenuItem newBoxGraph;

        newMeter = new JMenuItem("New Meter");
        newRoundMeter = new JMenuItem("New Round Meter");
        newLineGraph = new JMenuItem("New Line Graph");
        newBoxGraph = new JMenuItem("New Box Graph");

        setupMenuItemGraphics(newMeter);
        setupMenuItemGraphics(newRoundMeter);
        setupMenuItemGraphics(newLineGraph);
        setupMenuItemGraphics(newBoxGraph);

        add(newMeter);
        add(newRoundMeter);
        add(newLineGraph);
        add(newBoxGraph);

        newMeter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.addBlankMeter(xMeterPosition, yMeterPosition, 0); //creating new Meter object (Standard view: 0).
            }
        });

        newRoundMeter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.addBlankMeter(xMeterPosition, yMeterPosition, 1); //creating a new Meter object (Round view: 1).
            }
        });

        newLineGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.addBlankLineGraph(xMeterPosition, yMeterPosition); //creating a new LineGraph object.
            }
        });

        newBoxGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.addBlankBoxGraph(xMeterPosition, yMeterPosition); //creating a new BoxGraph object.
            }
        });
    }

    /**
     * Class constructor, Sets UI size defaults, populates right click menu.
     * @param x
     * @param y
     */
    public NewMeterMenu(int x, int y){
        this.setPreferredSize(new Dimension(200, 200));
        this.setBorder(BorderFactory.createEmptyBorder());

        populateMenu();

        this.xMeterPosition = x;
        this.yMeterPosition = y;
    }
}