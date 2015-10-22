package menus;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * MeterChangeListener Class
 * @author Aran Long
 *
 * This is a mouse listener used to handle creating MeterChanceMenu objects based on right
 * click presses.
 */
public class MeterChangeListener extends MouseAdapter{

    /**
     * Called on mouse release, calls doPop to create right click menu.
     * @param e
     */
     public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    /**
     * Creates MeterChangeMenu JPopupMenu object and sets position to right click location.
     * @param e
     */
    private void doPop(MouseEvent e){
        MeterChangeMenu menu = new MeterChangeMenu();

        Component component = e.getComponent();
        menu.setSource(component);

        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}
