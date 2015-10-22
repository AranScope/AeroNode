package uicomponents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

/**
 * Static Map Class (JPanel)
 * @author Aran Long
 *
 * This is the Static Map JPanel Class. This handles the downloading and displaying of static map data from the Google Maps
 * api given latitude and longitude data parsed in from either the telemetrics.SerialConnect object or the FligtLogger object.
 *
 * This is acheived with the use of an InputStream reader and an OutputStream writer. The static map image is
 * constructed from a byte array of pixel data received from the Google Maps api calls and is drawn to the included
 * paintComponent.
 */
public class StaticMap extends JPanel {
    private double latitude = 51.380000;
    private double longitude = -2.360000;

    private static Image mapImage;
    private static final double EARTH_RADIUS = 6372.8; // In kilometers
    private static int mapWidth = 510, mapHeight = 510;

    private Color foregroundColor = Ui.widgetForegroundGreen;

    /**
     * Class construcotr, Sets UI defaults (Size, Opacity, Position).
     * Assigns mouse listeners to enable dragging of StaticMap JPanel on 260px grid.
     */
    public StaticMap(){
        this.setSize(new Dimension(mapWidth,mapHeight));
        this.setOpaque(false);
        setPosition(latitude, longitude);

        Ui.assignMouseListeners(this);
    }

    /**
     * Draws background, top accent, Map Image to StaticMap JPanel.
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(foregroundColor);

        g2.drawImage(mapImage, 0, 0, this);
        g2.fillRect(0, 0, mapWidth,Ui.accentHeight);
    }

    /**
     * Sets color of top accent of StaticMap JPanel.
     * @param foregroundColor
     */
    public void setColor(Color foregroundColor){
        this.foregroundColor = foregroundColor;
    }

//    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        lat1 = Math.toRadians(lat1);
//        lat2 = Math.toRadians(lat2);
//
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
//        double c = 2 * Math.asin(Math.sqrt(a));
//        return EARTH_RADIUS* c;
//    }

    /**
     * Informs StaticMap class of the current position of the Plane (latitude, longitude).
     * Retrieves new Static map image from Google maps api based on current latitude and longitude using Input and Output streams.
     * Assigns global mapImage variable to parsed downloaded map data.
     *
     * @param latitude
     * @param longitude
     */
    public static void setPosition(double latitude, double longitude){
        try {
            String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=13&size=510x510&maptype=roadmap\\&markers=color:green%7C" + latitude + "," + longitude;
            String destinationFile = "res/tempmap.jpg";
            URL url = new URL(mapUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] readBytes = new byte[2048];
            int length;

            while ((length = is.read(readBytes)) != -1) {
                os.write(readBytes, 0, length);
            }
            is.close();
            os.close();
            mapImage = ImageIO.read(new File("res/tempmap.jpg"));
        } catch (IOException e) {
            try{
                mapImage = ImageIO.read(new File("res/tempmap.jpg"));
            }
            catch(IOException ex){
                System.out.println("StaticMap: Error loading tempmap.jpg map image\n" + ex.toString());
            }
        }
    }
}