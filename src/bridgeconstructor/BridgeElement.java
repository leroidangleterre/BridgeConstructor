package bridgeconstructor;

import colorramp.ColorRamp;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author arthu
 */
public abstract class BridgeElement {

    protected double x, y;
    protected double vx, vy;
    protected double rotationRate;

    private double angle;
    protected double length; // The default length, without any constraints.
    private double width;

    protected double mass;
    protected double inertiaMoment;

    protected double lineicMass;

    private double youngModulus;
    private double tension; // Positive in case of elongation, negative in case of contraction.
    private double forceMoments; // Sum of the moments of all forces applied to this element.

    public BridgeElement(double newX, double newY, double newAngle, double newLength) {
        x = newX;
        y = newY;
        vx = 0;
        vy = 0;
        rotationRate = 0;
        angle = newAngle;
        length = newLength;
        width = 0.2;
        youngModulus = 0;
        tension = 0;
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {

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
        g.setColor(this.getColor());
        g.fillPolygon(xAppTab, yAppTab, 4);

        int xApp = (int) ((xAppTab[2] + xAppTab[0]) / 2);
        int yApp = (int) ((yAppTab[2] + yAppTab[0]) / 2);

        g.setColor(Color.black);
        g.drawString(this.mass + "", xApp, yApp);
    }

    public abstract Color getColor();

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

    protected void setCenter(double newX, double newY) {
        x = newX;
        y = newY;
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

        double finalDistance;

        // When the angle of the plank is zero, A is the left end, B is the right end.
        double xA = getXA();
        double xB = getXB();
        double yA = getYA();
        double yB = getYB();

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

    /**
     * Get how much stress the element is under.
     *
     * @return the amount of tension received by the mechanical element.
     */
    public double getTension() {
        return 0;
    }

    public double getXA() {
        return x - length / 2 * Math.cos(angle);
    }

    public double getYA() {
        return y - length / 2 * Math.sin(angle);
    }

    public double getXB() {
        return x + length / 2 * Math.cos(angle);
    }

    public double getYB() {
        return y + length / 2 * Math.sin(angle);
    }

    /**
     * Receive a force (fx, fy) applied durig a time of dt;
     * change the velocity and rotation rate.
     *
     * @param fx x-coordinate of the force
     * @param fy y-coordinate of the force
     * @param dt duration of the period during which the force is applied
     */
    void receiveForce(double fx, double fy,
            double xApplication, double yApplication,
            double dt) {

        // Change in velocity:
        changeVelocity(fx, fy, dt);

        // Change in rotation rate:
        changeRotationRate(fx, fy, xApplication, yApplication, dt);
    }

    void changeVelocity(double fx, double fy, double dt) {

        // Change in velocity:
        this.vx += fx * dt;
        this.vy += fy * dt;
    }

    private void changeRotationRate(double fx, double fy,
            double xApp, double yApp,
            double dt) {

        double forceMoment = (xApp - x) * fy - (yApp - y) * fx;
//        System.out.println("forceMoment: " + forceMoment);

//        System.out.println("inertiaMoment: " + inertiaMoment);
        rotationRate += forceMoment / inertiaMoment;
    }

    /**
     * Compute the mass from the lineic mass, once the length is set.
     *
     */
    abstract void computeMassAndInertiaMoment();

    /**
     * Return true if this element shares a point with the given element, within
     * a specified margin.
     *
     * @param otherElement the second element we examine
     * @param b when true, we look at the first end of the current element
     * @param b0 when true, we look at the first end of the parameter element
     * @return
     */
    boolean sharePoint(BridgeElement otherElement, boolean b, boolean b0) {
//        be0.getXA() == be1.getXA() && be0.getYA() == be1.getYA()
        double xThis, yThis, xOther, yOther;
        if (b) {
            xThis = this.getXA();
            yThis = this.getYA();
        } else {
            xThis = this.getXB();
            yThis = this.getYB();
        }
        if (b0) {
            xOther = otherElement.getXA();
            yOther = otherElement.getYA();
        } else {
            xOther = otherElement.getXB();
            yOther = otherElement.getYB();
        }

        double margin = 0.01;
        return (Math.abs(xThis - xOther) <= margin && Math.abs(yThis - yOther) <= margin);
    }

    /**
     * Compute the position the object moves to, at its current speed.
     *
     * @param dt amount of time during which the object moves.
     */
    void moveAndRotateAtCurrentSpeed(double dt) {
        this.x += this.vx * dt;
        this.y += this.vy * dt;

        this.angle += this.rotationRate * dt;
    }

}
