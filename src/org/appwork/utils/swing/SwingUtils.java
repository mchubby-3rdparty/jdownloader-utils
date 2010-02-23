package org.appwork.utils.swing;

import java.awt.Component;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class SwingUtils {
    /**
     * Calculates the position of a frame to be in the center of an other frame.
     * 
     * @param parentFrame
     * @param frame
     * @return
     */
    public static Point getCenter(JFrame parentFrame, Window frame) {
        Point point = new Point();
        int x = 0, y = 0;

        if (parentFrame == null || frame == null) {
            point.setLocation(x, y);
            return point;
        }

        x = parentFrame.getLocation().x + (parentFrame.getSize().width / 2) - (frame.getSize().width / 2);
        y = parentFrame.getLocation().y + (parentFrame.getSize().height / 2) - (frame.getSize().height / 2);

        point.setLocation(x, y);

        return point;
    }

    /**
     * @param frame
     * @param string
     */
    public static JComponent getComponentByName(JComponent frame, String name) {
        JComponent ret = null;
        for (Component c : frame.getComponents()) {

            if (c instanceof JComponent) {
                if (c.getName() != null && c.getName().equals(name)) {
                    return (JComponent) c;
                } else {
                    ret = getComponentByName((JComponent) c, name);
                    if (ret != null) return ret;

                }
            }
        }
        return null;

    }
}
