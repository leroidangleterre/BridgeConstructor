package bridgeconstructor;

import colorramp.ColorRamp;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

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

    /**
     * Compute the distance between this plank and the point P of
     * coordinates(xP, yP);
     *
     * @param xP
     * @param yP
     * @return
     */
    double getDistance(double xP, double yP) {

        double finalDistance = -1; // Must be changed.

        // When the angle of the plank is zero, A is the left end, B is the right end.
        double xA = x - length / 2 * Math.cos(angle);
        double xB = x + length / 2 * Math.cos(angle);
        double yA = y - length / 2 * Math.sin(angle);
        double yB = y + length / 2 * Math.sin(angle);

        // Unit vector from A to B
        double uX = (xB - xA) / length;
        double uY = (yB - yA) / length;

        // Algebraic distance between A and the projection of Q(x,y) on [AB]:
        // If that distance is positive,
        // then the projection is on the correct side of A;
        // If that distance is less than the length of the segment,
        // then the projection is on the correct side of B.
        double projAB = (xP - xA) * uX + (yP - yA) * uY;

        if (projAB < 0) {
            // Projection is not in segment, point is closest to A
            finalDistance = Math.sqrt((xP - xA) * (xP - xA) + (yP - yA) * (yP - yA));
        } else if (projAB > length) {
            // Projection is not in segment, point is closest to B
            finalDistance = Math.sqrt((xP - xB) * (xP - xB) + (yP - yB) * (yP - yB));
        } else {
            // Projection is in segment
            finalDistance = Math.abs((xP - xA) * (-uY) + (yP - yA) * uX);
        }

        if (finalDistance == -1) {
            System.out.println("Error in distance: " + finalDistance);
            return -1;
        } else {
            return finalDistance;
        }
    }

    public String toString() {
        return "Plank centered at " + this.x + ", " + this.y;
    }

    private void paintColoredNeighbors(Graphics g, double x0, double y0, double zoom) {
        ColorRamp ramp = new ColorRamp();

        ramp.addValue(-this.length, Color.red);
        ramp.addValue(0, Color.blue);
        ramp.addValue(this.length, Color.yellow);
        ramp.addValue(1.5 * this.length, Color.green);

        for (int i = 0; i < 10000; i++) {
            double maxDistance = 100;
            double x = this.x + 2 * maxDistance * (new Random().nextDouble() - 0.5);
            double y = this.y + 2 * maxDistance * (new Random().nextDouble() - 0.5);

            double distance = this.getDistance(x, y);

            int radius = 3;
            g.setColor(ramp.getValue(distance));
            int xApp = (int) (x0 + x * zoom);
            int yApp = (int) (g.getClipBounds().height - (y0 + y * zoom));
            g.fillOval(xApp - radius, yApp - radius, 2 * radius, 2 * radius);
        }
    }

}
