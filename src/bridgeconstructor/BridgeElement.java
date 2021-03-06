package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    // Position of the element at its creation
    private double startX, startY, startVx, startVy, startAngle, startLength, startRotationRate;

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

        startX = x;
        startY = y;
        startVx = vx;
        startVy = vy;
        startLength = length;
        startAngle = angle;
        startRotationRate = rotationRate;
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

    /**
     * Get how much stress the element is under.
     *
     * @return the amount of tension received by the mechanical element.
     */
    public double getTension() {
        return tension;
    }

    public void resetTension() {
        tension = 0;
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

        // Change in tension
        changeInnerTension(fx, fy, xApplication, yApplication, dt);
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
        rotationRate += forceMoment / inertiaMoment;
    }

    private void changeInnerTension(double fx, double fy,
            double xApplication, double yApplication,
            double dt) {

        double xA = getXA();
        double xB = getXB();
        double yA = getYA();
        double yB = getYB();

        // Unit vector from A to B
        double uX = (xB - xA) / length;
        double uY = (yB - yA) / length;

        double dFElongation = 0;

        if (isCloseToA(xApplication, yApplication)) {
            dFElongation = -(fx * uX + fy * uY);
        } else {
            dFElongation = fx * uX + fy * uY;
        }

        tension += dFElongation;
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

    abstract void dampenSpeed(double dt);

    void stopMovement() {
        this.vx = 0;
        this.vy = 0;
        this.rotationRate = 0;
    }

    /**
     * Go back to the original location and orientation
     *
     */
    void restart() {
        x = startX;
        y = startY;
        vx = startVx;
        vy = startVy;
        angle = startAngle;
        length = startLength;
        rotationRate = startRotationRate;
    }

    /**
     * Tell whether a given point is closer to A or to B.
     *
     * @param xC
     * @param yC
     * @return true when C is closer to A than to B, false otherwise.
     */
    private boolean isCloseToA(double xC, double yC) {

        double xA = getXA();
        double xB = getXB();
        double yA = getYA();
        double yB = getYB();

        // Compare the squares of the distances
        double dAsquared = (xA - xC) * (xA - xC) + (yA - yC) * (yA * yC);
        double dBsquared = (xB - xC) * (xB - xC) + (yB - yC) * (yB * yC);

        return (dAsquared < dBsquared);
    }

    void save(BufferedWriter writer) {
        try {
            writer.write(this.getClass() + " " + x + " " + y + " " + angle + " " + length + "\n");
        } catch (IOException ex) {
            Logger.getLogger(Plank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
