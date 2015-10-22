package menus;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * NewMeterListener Class
 * @author Aran Long
 *
 * This is a mouse listener used to handle creating NewMeterMenu objects based on right
 * click presses.
 */
public class NewMeterListener extends MouseAdapter {

    /**
     * Called on mouse release, calls doPop to create right click menu.
     * @param e
     */
    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    /**
     * Creates NewMeterMenu JPopupMenu object and sets position to right click location.
     * @param e
     */
    private void doPop(MouseEvent e){
        NewMeterMenu menu = new NewMeterMenu(e.getX(), e.getY());
        Component component = e.getComponent();
        menu.show(component, e.getX(), e.getY());
    }
}