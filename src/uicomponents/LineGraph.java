package uicomponents;

import menus.MeterChangeListener;
import telemetrics.Sensor;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * LineGraph Class (JPanel)
 * @author Aran Long
 *
 * This is the uicomponents.LineGraph JPanel class (Template for uicomponents.LineGraph objects) this takes input either from an assigned telemetrics.Sensor or
 * from manually assigned values should a sensor not be present. With each new value received a data point will be added
 * to the dataSet array and displayed within the paintComponent. When a new data point is received the dataSet will shift
 * one index to the left allowing the graph to scroll towards the left. Each of these data points will be joined with
 * a BasicStroke 2px wide line. When incoming data is larger than all previous values, all values will be linearly scaled
 * down such that the new maximum value is at the top of the graph.
 *
 * The Line Graph Class includes a Mouse Motion Listener to allow for it to be dragged within it's parent JFrame.
 */
public class LineGraph extends JPanel {
    private FontMetrics fm;

    private int graphWidth = 510, graphHeight = 250;
    private int graphSideMargin = 35;
    private int graphLabelMargin = 27;
    private int graphTopMargin = 20;
    private int graphBottomMargin = 30;
    private int graphLineHeight = graphHeight - graphTopMargin - graphBottomMargin;
    private int graphPointSpacing = 25;
    private int graphPointDiameter = 6;


    private String graphName;
    private double[] dataSet = new double[18];
    private double maxValue = 0.000000000001;
    private Sensor sensor;

    private Color foregroundColor = Ui.widgetForegroundGreen;

    /**
     * Sets string name of LineGraph object.
     * @param name
     */
    public void setName(String name){
        graphName = name;
    }

    /**
     * Assigns Sensor to LineGraph object, determinant of data source, title and units of the LineGraph object.
     * @param parseSensor
     */
    public void setSensor(Sensor parseSensor){
        sensor = parseSensor;
        graphName = sensor.getName();
        graphName += " (" + sensor.getUnits() + ")";
    }

    /**
     * Sets Color of foreground elements (bars, labels, titles) of graph.
     *
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
     * Calls addListeners Method to add standard MouseDrag listeners to LineGraph.
     */
    public LineGraph(){
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
    public void  addValue(double value){
        for(int x = 0; x< dataSet.length - 1; x++){
            dataSet[x] = dataSet[x+1];
        }

        dataSet[dataSet.length-1] = value;

        if(value > maxValue) maxValue = value;
    }

    /**
     * Draws Bars, Axis, Axis Labels, Titles of LineGraph.
     * Linearly scales Point heights such that no Point exceeds the height
     * of the graph and the maxValue Point is the height of the graph.
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Adding new data values to graph should the assigned sensor be valid and the new value not be exactly equal to the previous value
        if(sensor!= null && !(dataSet[dataSet.length - 1] == sensor.getValue())) addValue(sensor.getValue());

//        if(!Controller.busy){
//            for(int x = 0; x< dataSet.length; x++){
//                dataSet[x] = 0;
//            }
//        }

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0,0,graphWidth,graphHeight);

        //drawing line and points on graph (linear scaling)
        BasicStroke st = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2.setStroke(st);

        g2.setColor(foregroundColor);

        int nPoints = dataSet.length;
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];

        for(int x = 0; x < nPoints; x++){
            xPoints[x] = (x*graphPointSpacing) + graphSideMargin;
        }

        for(int y = 0; y < nPoints; y++){
            yPoints[y] = (int)(graphHeight - graphBottomMargin - (dataSet[y] * (graphLineHeight/ maxValue)));
            g2.fillOval(xPoints[y] - graphPointDiameter/2, yPoints[y] - graphPointDiameter/2, graphPointDiameter, graphPointDiameter);
        }

        g2.drawPolyline(xPoints, yPoints, nPoints);

        //drawing graph title
        g2.setFont(Ui.ftMid);
        fm = g2.getFontMetrics();
        g2.drawString(graphName, graphWidth/2 - fm.stringWidth(graphName)/2, graphHeight - fm.getAscent()/2);

        //drawing y-axis units
        g2.setFont(Ui.ftSml);
        fm = g2.getFontMetrics();
        DecimalFormat df = new DecimalFormat("#0.00");
        for(double x = maxValue; x>=0; x-=(maxValue /10)) {
            g2.drawString("" + df.format(x), graphLabelMargin - fm.stringWidth(String.valueOf(df.format(x))), (int)(graphLineHeight- ((x*graphLineHeight/ maxValue)) + graphTopMargin + fm.getAscent()/2));
        }
    }
}