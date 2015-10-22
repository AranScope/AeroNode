package uicomponents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * ColorPicker (JPanel)
 * @author Aran Long
 *
 * The ColorPicker class allows the User to select a color from a loaded image and is utilised by other JPanel classes to change
 * foreground Colors.
 */
public class ColorPicker extends JPanel {
    private BufferedImage image = null;
    private Color color;

    /**
     * Class constructor, Sets up default UI configurations.
     * Loads Image used for ColorPicker.
     * Adds MouseListener used to retrieve Color from ColorPicker Image.
     */
    public ColorPicker() {
        color = new Color(255,255,255,255);
        try {
            image = ImageIO.read(new File("res/ColorWheel.png"));
        }catch(Exception e){e.printStackTrace();}
        this.setSize(image.getWidth(), image.getHeight());
        this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

        this.setOpaque(false);
        this.setBackground(color);

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                color = new Color(image.getRGB(e.getX(), e.getY()));
                repaint();
            }
        });
    }

    /**
     * Returns current Color selected.
     * @return
     */
    public Color getColor(){
        return color;
    }

    /**
     * Draws ColorPicker Image to JPanel paintComponent.
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(image, 0,0,null);
    }
}
