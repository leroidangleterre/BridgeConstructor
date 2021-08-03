package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthu
 */
public class Spring extends BridgeElement {

    // The spring links elastically two elements, and its rest length is always zero.
    private BridgeElement target0;
    private boolean isLinkedToFirstEndOfTarget0;
    private BridgeElement target1;
    private boolean isLinkedToFirstEndOfTarget1;

    // Coordinates of the points where the spring is attached:
    double x0, y0, x1, y1;

    private double stiffness;

    public Spring(double newX, double newY, double newAngle, double newLength) {
        super(newX, newY, newAngle, newLength);
        stiffness = 30;
    }

    public Spring() {
        this(0, 0, 0, 0);
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }

    @Override
    public void paint(Graphics g, double x0, double y0, double zoom) {

        double xTarget0 = (isLinkedToFirstEndOfTarget0 ? target0.getXA() : target0.getXB());
        double yTarget0 = (isLinkedToFirstEndOfTarget0 ? target0.getYA() : target0.getYB());
        int xApp0 = (int) (x0 + xTarget0 * zoom);
        int yApp0 = (int) (g.getClipBounds().height - (y0 + yTarget0 * zoom));

        double xTarget1 = (isLinkedToFirstEndOfTarget1 ? target1.getXA() : target1.getXB());
        double yTarget1 = (isLinkedToFirstEndOfTarget1 ? target1.getYA() : target1.getYB());
        int xApp1 = (int) (x0 + xTarget1 * zoom);
        int yApp1 = (int) (g.getClipBounds().height - (y0 + yTarget1 * zoom));

        g.setColor(this.getColor());
        g.drawLine(xApp0, yApp0, xApp1, yApp1);
    }

    void setTargets(BridgeElement e0, boolean isFirst0, BridgeElement e1, boolean isFirst1) {
        target0 = e0;
        isLinkedToFirstEndOfTarget0 = isFirst0;
        target1 = e1;
        isLinkedToFirstEndOfTarget1 = isFirst1;
    }

    private void computeTargetCoordinates() {
        if (isLinkedToFirstEndOfTarget0) {
            x0 = target0.getXA();
            y0 = target0.getYA();
        } else {
            x0 = target0.getXB();
            y0 = target0.getYB();
        }
        if (isLinkedToFirstEndOfTarget1) {
            x1 = target1.getXA();
            y1 = target1.getYA();
        } else {
            x1 = target1.getXB();
            y1 = target1.getYB();
        }
    }

    @Override
    public double getTension() {
        double tension = stiffness * getLength();
        return tension;
    }

    private double getLength() {
        computeTargetCoordinates();
        double dx = x1 - x0;
        double dy = y1 - y0;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Apply the tension on both targets of this spring.
     *
     */
    void applyForce(double dt) {

        // Unit vector ux goes from (x0,y0) to (x1,y1) and has norm one.
        double currentLength = getLength();
        if (currentLength != 0) {
            double utx = (x1 - x0) / currentLength;
            double uty = (y1 - y0) / currentLength;

            double tension = getTension();
            target0.receiveForce(tension * utx, tension * uty,
                    x0, y0,
                    dt);
            target1.receiveForce(-tension * utx, -tension * uty,
                    x1, y1,
                    dt);
        }
        // If length is zero, do nothing.
    }

    @Override
    void computeMassAndInertiaMoment() {
        System.out.println("Spring mass and inertia moment unused.");
    }
}
