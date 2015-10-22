package uicomponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Top Bar Class (JPanel)
 * @author Aran Long
 *
 * This is the Top Bar JPanel Class. This handles all of the standard functions of a windows frame. These include: Minimisation,
 * Maximisation and Closing of the JFrame. This also includes a Mouse Motion Listener to allow for the main JFrame (which is parsed
 * into the constructor) to be moved when the uicomponents.TopBar is dragged. The paintComponent contains the styling for the uicomponents.TopBar acheived using
 * GradientPaints and simple J2D filled shapes.
 */
public class TopBar extends JPanel {
    public JFrame frame;
    private boolean exitHover = false;
    private boolean maxHover = false;
    private boolean minHover = false;
    private int px, py;

    public TopBar(JFrame parseFrame){
        frame = parseFrame;
        this.setOpaque(false);
        this.setSize(new Dimension(2000, 40));
        this.setLayout(null);

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                frame.setLocation(e.getLocationOnScreen().x - px, e.getLocationOnScreen().y - py);
                Ui.framePosX = e.getLocationOnScreen().x - px;
                Ui.framePosY = e.getLocationOnScreen().y - py;
            }

            public void mouseMoved(MouseEvent e){
                Point current = new Point(e.getX(), e.getY());
                exitHover = current.getX() > 12 && current.getX() < 28 && current.getY() > 12 && current.getY() < 28;

                maxHover = current.getX() > 34 && current.getX() < 50 && current.getY() > 12 && current.getY() < 28;

                minHover = current.getX() > 56 && current.getX() < 72 && current.getY() > 12 && current.getY() < 28;
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                px = e.getX();
                py = e.getY();
            }

            @Override
            public void mouseClicked(MouseEvent e){
                if(exitHover){
                    System.exit(0);
                }
                if(maxHover){
                    if(frame.getContentPane().getWidth() > 1305) { //if already maximised
                        frame.setSize(new Dimension(1305, 830));
                        Ui.framePosX = frame.getX();
                        Ui.framePosY = frame.getY();
                    }
                    else{
                        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                        Ui.framePosX = frame.getX();
                        Ui.framePosY = frame.getY();
                    }
                    frame.repaint();
                }
                if(minHover){
                    frame.setState(Frame.ICONIFIED);
                    minHover = false;
                    frame.repaint();
                }
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle r = new Rectangle();
        r.setBounds(0, 0, 2000, 40);
        g2.setPaint(new GradientPaint(0,0,Color.decode("0xF8FBF9"), 0, 40, Color.decode("0xD7D7D7")));
        g2.fill(r);

        if(exitHover){
            g2.setColor(Ui.widgetForegroundRed);
        }
        else g2.setColor(Color.decode("0xCDCDCD"));
        g2.fillOval(12,12,16,16);

        if(maxHover){
            g2.setColor(Ui.widgetForegroundOrange);
        }
        else g2.setColor(Color.decode("0xCDCDCD"));
        g2.fillOval(34,12,16,16);


        if(minHover){
            g2.setColor(Ui.widgetForegroundGreen);
        }
        else g2.setColor(Color.decode("0xCDCDCD"));
        g2.fillOval(56,12,16,16);
    }
}