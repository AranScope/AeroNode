package uicomponents;

import menus.MeterChangeListener;
import telemetrics.Sensor;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Box Graph Class (JPanel)
 * @author Aran Long
 *
 * This is the uicomponents.BoxGraph JPanel class (Template for uicomponents.BoxGraph objects) this takes input either from an assigned telemetrics.Sensor or
 * from manually assigned values should a sensor not be present. With each new value received a data point will be added
 * to the dataSet array and displayed within the paintComponent. When a new data point is received the dataSet will shift
 * one index to the left allowing the graph to scroll towards the left. Each of these data points be represented by rectangular
 * bars within the paintComponent. When incoming data is larger than all previous values, all values will be linearly scaled
 * down such that the new maximum value is at the top of the graph.
 *
 * The Box Graph Class includes a Mouse Motion Listener to allow for it to be dragged within it's parent JFrame.
 */
public class BoxGraph extends JPanel {
    private FontMetrics fm;

    private int graphWidth = 510, graphHeight = 250;
    private int graphBarWidth = 15;
    private int graphSideMargin = 35;
    private int graphLabelMargin = 27;
    private int graphTopMargin = 20;
    private int graphBottomMargin = 30;
    private int graphBarHeight = graphHeight - graphTopMargin - graphBottomMargin;
    private int graphBarSpacing = 10;
    private String graphName;

    private  double[] dataSet = new double[18];
    private double maxValue = 0.000000000001;
    private Sensor sensor;

    private Color foregroundColor = Ui.widgetForegroundGreen;

    /**
     * Sets String name of BoxGraph object.
     *
     * @param name
     */
    public void setName(String name){
        graphName = name;
    }

    //assigns sensor to local sensor variable, this determines the data source, title and units of the graph

    /**
     * Assigns Sensor to BoxGraph object, determinant of data source, title and units of the BoxGraph object.
     * @param sensor
     */
    public void setSensor(Sensor sensor){
        this.sensor = sensor;
        graphName = sensor.getName();
        graphName += " (" + sensor.getUnits() + ")";
    }

    /**
     * Sets Color of foreground elements (bars, labels, titles) of graph.
     * @param foregroundColor
     */
    public void setColor(Color foregroundColor){
        this.foregroundColor = foregroundColor;
    }

    /**
     * Adds MeterChangeListener, MouseDragListeners to Graph.
     */
    private void addListeners(){
        this.addMouseListener(new MeterChangeListener());
        Ui.assignMouseListeners(this);
    }

    /**
     * Class constructor, Sets UI defaults.
     * Calls addListeners Method to add standard MouseDrag listeners to BoxGraph.
     */
    public BoxGraph(){
        this.setOpaque(false);
        this.setSize(new Dimension(graphWidth,graphHeight));
        addListeners();
    }

    /**
     * Adds new Value to Graph data set.
     * New Value entered at final index.
     * All other values shifted one index left.
     * Value at first index removed from data set.
     * Reassigns maxValue if new value is greater than current maxValue.
     * @param value
     */
    private void addValue(double value){
        for(int x = 0; x< dataSet.length - 1; x++){
            dataSet[x] = dataSet[x+1];
        }

        dataSet[dataSet.length-1] = value;
        if(value > maxValue) maxValue = value;
    }

    /**
     * Draws Bars, Axis, Axis Labels, Titles of BarGraph.
     * Linearly scales Bar heights such that no Bar exceeds the height
     * of the graph and the maxValue Bar is the height of the graph.
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if(sensor!= null && !(dataSet[dataSet.length - 1] == sensor.getValue())) addValue(sensor.getValue());

//        if(!Controller.busy){
//            for(int x = 0; x< dataSet.length; x++){
//                dataSet[x] = 0;
//            }
//        }

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0, 0, graphWidth, graphHeight);

        //drawing background bars
        g2.setColor(Ui.widgetForegroundGray);
        for(int x = graphSideMargin; x < graphWidth-graphSideMargin; x += graphBarWidth + graphBarSpacing){
            g2.fillRect(x, graphTopMargin, graphBarWidth, graphBarHeight);
        }

        int count = 0;
        //drawing foreground bars based on data values and linear scaling based upon maximum value
        g2.setColor(foregroundColor);
        for(int x = graphSideMargin; x < graphWidth - graphSideMargin; x += graphBarWidth + graphBarSpacing){
            g2.fillRect(x, (int)(graphHeight - graphBottomMargin - ((dataSet[count] * graphBarHeight)/ maxValue)), graphBarWidth, (int)((dataSet[count] * graphBarHeight)/ maxValue));
            count++;
        }

        g2.setFont(Ui.ftMid);
        fm = g2.getFontMetrics();

        //drawing graph title and units
        g2.drawString(graphName, graphWidth/2 - fm.stringWidth(graphName)/2, graphHeight - fm.getAscent()/2);

        //drawing y-axis of graph
        g2.setFont(Ui.ftSml);
        fm = g2.getFontMetrics();
        DecimalFormat df = new DecimalFormat("#0.00");

        for(double x = maxValue; x>=0; x-=(maxValue /10)) {
            g2.drawString("" + df.format(x), graphLabelMargin - fm.stringWidth(String.valueOf(df.format(x))), (int)(graphBarHeight- ((x*graphBarHeight/ maxValue)) + graphTopMargin + fm.getAscent()/2));
        }
    }
}