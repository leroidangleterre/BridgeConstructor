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
    private double width;

    private double mass;
    private double inertiaMoment;

    public Plank() {

    }

    public Plank(double newX, double newY, double newAngle, double newLength) {
        x = newX;
        y = newY;
        angle = newAngle;
        length = newLength;
        width = 0.2;
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {

        int lengthApp = (int) (length * zoom);
        int widthApp = Math.max(lengthApp / 10, 3);

        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // World-coordinates of the corners of the plank
        double xTab[] = {
            x + length / 2 * c - width / 2 * s, // xA
            x + length / 2 * c + width / 2 * s, // xB
            x - length / 2 * c + width / 2 * s, // xC
            x - length / 2 * c - width / 2 * s // xD
        };

        double yTab[] = {
            y + length / 2 * s + width / 2 * c, // yA
            y + length / 2 * s - width / 2 * c, // yB
            y - length / 2 * s - width / 2 * c, // yC
            y - length / 2 * s + width / 2 * c // yD
        };

        // Apparent coordinates of the points
        int xAppTab[] = {
            (int) (x0 + xTab[0] * zoom),
            (int) (x0 + xTab[1] * zoom),
            (int) (x0 + xTab[2] * zoom),
            (int) (x0 + xTab[3] * zoom)};
        int h = g.getClipBounds().height;
        int yAppTab[] = {
            (int) (h - (y0 + yTab[0] * zoom)),
            (int) (h - (y0 + yTab[1] * zoom)),
            (int) (h - (y0 + yTab[2] * zoom)),
            (int) (h - (y0 + yTab[3] * zoom))};
        g.setColor(Color.red);
        g.fillPolygon(xAppTab, yAppTab, 4);
    }

    /**
     * Set the coordinates of the two ends of the plank,
     * instead of the center's x-y, angle and length.
     *
     * @param xBegin
     * @param yBegin
     * @param xEnd
     * @param yEnd
     */
    void setEndCoords(double xBegin, double yBegin, double xEnd, double yEnd) {
        this.x = (xBegin + xEnd) / 2;
        this.y = (yBegin + yEnd) / 2;

        double dx = xEnd - xBegin;
        double dy = yEnd - yBegin;
        this.length = Math.sqrt(dx * dx + dy * dy);

        this.angle = Math.atan2(dy, dx);
    }

}
