package uicomponents;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * PlaneView Class (JPanel)
 * @author Aran Long
 *
 * PlaneView is a visual interface using the OpenGL graphics library. It displays a 3D representation
 * of the Plane and shows its rotation in 3-Axis in real time.
 */
public class PlaneView extends JPanel implements GLEventListener {

    private GLU glu = new GLU();

    public static float xRotation = 30f;
    public static float yRotation = 0f;
    public static float zRotation = 0f;

    public static float centrex = 0.0f;
    public static float centrey = 0.0f;

    public static GLCanvas glcanvas;

    private int xoffset, yoffset;
    private int px, py;

    /**
     * Draws Cuboid using GL_QUADS of width: w, height: h, Depth: d, X-Centre: cx, Y-Centre: cy, Z-Centre: Z of Color: color.
     *
     * @param w Width of cuboid
     * @param h Height of cuboid.
     * @param d Depth of cuboid.
     * @param cx X-Centre of cuboid.
     * @param cy Y-Centre of cuboid.
     * @param cz Z-Centre of cuboid.
     * @param gl GL2 Object to draw QUADS, Vertices.
     * @param color Color of cuboid.
     */
    private void drawCuboid(float w, float h, float d, float cx, float cy, float cz, GL2 gl, Color color){
        gl.glColor3f( color.getRed()/255.0f,color.getGreen()/255.0f,color.getBlue()/255.0f);

        //front face
        gl.glBegin( GL2.GL_QUADS );
        gl.glVertex3f(cx - w/2, cy - h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy - h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy + h/2, cz-d/2);
        gl.glVertex3f(cx - w/2, cy + h/2, cz-d/2);

        //top face
        gl.glBegin( GL2.GL_QUADS );
        gl.glVertex3f(cx - w/2, cy + h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy + h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy + h/2, cz+d/2);
        gl.glVertex3f(cx - w/2, cy + h/2, cz+d/2);

        //back face
        gl.glBegin( GL2.GL_QUADS );
        gl.glVertex3f(cx - w/2, cy - h/2, cz+d/2);
        gl.glVertex3f(cx + w/2, cy - h/2, cz+d/2);
        gl.glVertex3f(cx + w/2, cy + h/2, cz+d/2);
        gl.glVertex3f(cx - w/2, cy + h/2, cz+d/2);

        //bottom face
        gl.glBegin( GL2.GL_QUADS );
        gl.glVertex3f(cx - w/2, cy - h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy - h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy - h/2, cz+d/2);
        gl.glVertex3f(cx - w/2, cy - h/2, cz+d/2);

        //right face
        gl.glBegin( GL2.GL_QUADS );
        gl.glVertex3f(cx + w/2, cy - h/2, cz-d/2);
        gl.glVertex3f(cx + w/2, cy - h/2, cz+d/2);
        gl.glVertex3f(cx + w/2, cy + h/2, cz+d/2);
        gl.glVertex3f(cx + w/2, cy + h/2, cz-d/2);

        //left face
        gl.glBegin( GL2.GL_QUADS );
        gl.glVertex3f(cx - w/2, cy - h/2, cz-d/2);
        gl.glVertex3f(cx - w/2, cy - h/2, cz+d/2);
        gl.glVertex3f(cx - w/2, cy + h/2, cz+d/2);
        gl.glVertex3f(cx - w/2, cy + h/2, cz-d/2);
    }

    /**
     * Gets GL2 Object from GLAutoDrawable object to enable Drawing of GL geometry.
     * Translates GL canvas.
     * Rotates GL origin based on xRotation, yRotation, zRotation values. ie. Plane rotation.
     * Draws body of plane with drawCuboid method.
     * Ends and Flushes GL Object when drawing is finished.
     *
     * @param drawable
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0f, 0f, -8.0f);

        gl.glRotatef(xRotation,1.0f,0.0f,0.0f);
        gl.glRotatef(yRotation,0.0f,1.0f,0.0f);
        gl.glRotatef(zRotation,0.0f,0.0f,1.0f);

        //giving different colors to different sides
        Color c = Color.decode("0x95a5a6");
        Color cl = Color.white;
        //main body
        drawCuboid(0.8f, 1, 6, centrex, centrey, 0, gl, cl);
        //wings
        drawCuboid(8f, 0.1f, 1.2f, centrex, centrey, -0.4f, gl, c);
        //h tail
        drawCuboid(3, 0.1f, 0.8f, centrex, centrey, 2.55f, gl, c);
        //v tail
        drawCuboid(0.1f, 1.5f, 0.6f, centrex, centrey+0.75f, 2.65f, gl, c);

        gl.glEnd();
        gl.glFlush();
    }

    /**
     * Auto generated dispose Method. Unused.
     *
     * @param drawable
     */
    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    /**
     * Initialisation, Gets GL2 Object from GLAutoDrawable object to enable Drawing of GL geometry.
     * Sets Clear Color, Clear depth.
     * Enables GL Depth Test.
     * Sets GL Depth Function.
     * Adds Anti-Aliasing and Perspective correction.
     *
     * @param drawable
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.26274509803f, 0.26666666666f, 0.30980392156f, 0.0f);
        gl.glClearDepth(1.0f );
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_NICEST);
    }

    /**
     * Reshape method, Called on program start, Resize, Reshaping of window.
     * Maps the new size of the window with glViewPort based on width and height parameters.
     * Sets Matrix mode of GL object.
     * Sets Perspective of GL window.
     *
     * @param drawable
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL2 gl = drawable.getGL().getGL2();
        if(height <=0) height =1;

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(75.0f, h, 1.0, 50.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Class constructor, Creates GL Canvas, Adds GL Canvas to JPanel.
     * Sets UI defaults (Layout, Size).
     * Adds MouseMotion Listener to enable dragging of UI element and snapping to 260px grid.
     */
    public PlaneView(){
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        // The canvas
        glcanvas = new GLCanvas(capabilities);
        glcanvas.addGLEventListener(this);
        glcanvas.setSize(510, 510);
        glcanvas.setLocation(0, 10);
        this.setLayout(null);
        this.add(glcanvas);
        this.setSize(new Dimension(510, 510));

        glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int xloc = e.getLocationOnScreen().x - px - Ui.widgetPanelX - Ui.framePosX;
                int yloc = e.getLocationOnScreen().y - py - Ui.widgetPanelY - Ui.framePosY;
                setLocation(xloc, yloc);

                xoffset = Ui.xmargin + (int) (Math.rint((double) xloc / 260) * 260);
                yoffset = Ui.ymargin + (int) (Math.rint((double) yloc / 260) * 260);
            }
        });

        glcanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                px = e.getX();
                py = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setLocation(xoffset, yoffset);
            }
        });
    }

    /**
     * Draws Background, Top accent of JPanel.
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Ui.widgetBackground);
        g2.fillRect(0, 0, 510, 510);

        g2.setColor(Ui.widgetForegroundGreen);
        g2.fillRect(0, 0, 510, 10);
    }
}
