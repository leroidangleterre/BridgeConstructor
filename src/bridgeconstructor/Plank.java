package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthu
 */
public class Plank {

    private double x, y, angle;
    private double length;

    private double mass;
    private double inertiaMoment;

    public Plank() {

    }

    public Plank(double newX, double newY, double newAngle, double newLength) {
        x = newX;
        y = newY;
        angle = newAngle;
        length = newLength;
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {

        int lengthApp = (int) (length * zoom);
        int widthApp = Math.max(lengthApp / 10, 3);
        int xApp = (int) (x0 + x * zoom);
        int yApp = (int) (g.getClipBounds().height - (y0 + y * zoom));
        g.setColor(Color.red);
        g.fillRect(xApp - lengthApp / 2, yApp, lengthApp, widthApp);
    }

}
