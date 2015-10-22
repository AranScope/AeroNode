package uicomponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

/**
 * Ui Class
 * @author Aran Long
 *
 * This is the uicomponents.Ui Class. This contains uicomponents.Ui data such as Colors and Font's that are used globally within the program and must
 * hence, have a global scope. This allows for quickly changing the Color and Font schemes of the entire program and reduces
 * the use of contextual local variables.
 */

//try not to get electrocuted by all this static.
public class Ui {
    public static Dimension screenSize;

    //default colors
    public static final Color widgetBackground = Color.decode("0x43444F");
    public static final Color widgetForegroundBlue = Color.decode("0x05D5FF");
    public static final Color widgetForegroundGreen = Color.decode("0xBADA55");
    public static final Color widgetForegroundRed = Color.decode("0xDD5145");
    public static final Color widgetForegroundWhite = Color.decode("0xF2F2F2");
    public static final Color widgetForegroundGray = Color.decode("0x606270");
    public static final Color widgetForegroundLightGray = Color.decode("0xA1A1A1");
    public static final Color widgetForegroundOrange = Color.decode("0xFFB50B");
    public static final Color frameBackground = Color.decode("0x31323A");
    public static final Color caretColor = Color.decode("0xD0CED6");

    /**
     * Returns a random Color from a set of the default UI colors.
     *
     * @return
     */
    public static Color randomColor(){
        Random ra = new Random();
        int index = ra.nextInt(3);
        switch(index){
            case 0: return widgetForegroundBlue;
            case 1: return widgetForegroundGreen;
            case 2: return widgetForegroundWhite;
        }
        return null;
    }

    //default fonts
    public static final Font ftSml = new Font("Seruf", Font.PLAIN, 10);
    public static final Font ftSide = new Font("Seruf", Font.PLAIN , 19);
    public static final Font ftMid = new Font("Seruf", Font.PLAIN, 20);
    public static final Font ftLrg = new Font("Seruf", Font.PLAIN, 90);

    public static final int meterWidth = 250, meterHeight = 250;
    public static final int accentHeight = 10;
    public static final int xmargin = 10, ymargin = 10;

    //default grid positions
    public static Point[] position = {new Point(10, 10),  new Point(270, 10),  new Point(530,10),  new Point(790, 10),
                                      new Point(10, 270), new Point(270, 270), new Point(530,270), new Point(790, 270),
                                      new Point(10, 530), new Point(270, 530), new Point(530,530), new Point(790, 530)};

    public static int framePosX = 0;
    public static int framePosY = 0;

    public static int widgetPanelX = 255;
    public static int widgetPanelY = 40;

    static int clickPointX, clickPointY;
    static int currentMouseButton = -1;

    /**
     * Assigns Mouse listeners to enable dragging of passed JPanel object on 260px grid with 10px margins.
     *
     * @param source
     */
    public static void assignMouseListeners(final JPanel source){
        //motion listeners to allow graph to be dragged around the screen.
        MouseMotionAdapter mouseDragListener = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(currentMouseButton == 1) {
                    int xloc = e.getLocationOnScreen().x - clickPointX - Ui.widgetPanelX - Ui.framePosX;
                    int yloc = e.getLocationOnScreen().y - clickPointY - Ui.widgetPanelY - Ui.framePosY;
                    source.setLocation(xloc, yloc);

                    source.repaint();
                }
            }
        };

        //Determines point clicked by user, only allows dragging if left click button held, sets xoffset, yoffset values from global 0,0 point.
        MouseAdapter mouseClickListener = new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                clickPointX = e.getX();
                clickPointY = e.getY();
                currentMouseButton = e.getButton();
            }

            //Snaps position of JPanel to nearest valid location on 260px x 260px grid.
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(currentMouseButton == 1) {
                    int xloc = e.getLocationOnScreen().x - clickPointX - Ui.widgetPanelX - Ui.framePosX;
                    int yloc = e.getLocationOnScreen().y - clickPointY - Ui.widgetPanelY - Ui.framePosY;
                    int xoffset = Ui.xmargin + (int) (Math.rint((double) xloc / 260) * 260);
                    int yoffset = Ui.ymargin + (int) (Math.rint((double) yloc / 260) * 260);
                    source.setLocation(xoffset, yoffset);
                }
                currentMouseButton = -1;
            }
        };

        //Adding mouse listeners to source JPanel object.
        source.addMouseMotionListener(mouseDragListener);
        source.addMouseListener(mouseClickListener);
    }
}