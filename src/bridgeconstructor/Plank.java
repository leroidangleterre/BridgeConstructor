package bridgeconstructor;

import colorramp.ColorRamp;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthu
 */
public class Plank extends BridgeElement {

    private colorramp.ColorRamp ramp;

    private double frictionCoef;

    public Plank(double newX, double newY, double newAngle, double newLength) {
        super(newX, newY, newAngle, newLength);
        // ramp = new ColorRamp();
        // ramp.addValue(0, new Color(167, 116, 73)); // Default brown
        // ramp.addValue(1, new Color(185, 71, 0)); // Rust

        lineicMass = 10;

        frictionCoef = 0.0;

        ramp = new ColorRamp();
        ramp.addValue(-100, Color.red);
        ramp.addValue(-10, Color.orange);
        ramp.addValue(0, Color.gray);
        ramp.addValue(10, Color.blue);
        ramp.addValue(100, Color.MAGENTA);
    }

    @Override
    void computeMassAndInertiaMoment() {
        this.mass = lineicMass * length;
        this.inertiaMoment = mass * length * length / 12;
    }

    @Override
    public Color getColor() {
        return ramp.getValue(this.getTension());
    }

    @Override
    public double getTension() {
        return super.getTension();
    }

    @Override
    void dampenSpeed(double dt) {
        this.vx -= vx * frictionCoef / this.mass;
        this.vy -= vy * frictionCoef / this.mass;
    }

    @Override
    void receiveForce(double fx, double fy,
            double xApplication, double yApplication,
            double dt) {
        super.receiveForce(fx, fy, xApplication, yApplication, dt);
    }

    @Override
    public void paint(Graphics g, double x0, double y0, double zoom) {
        super.paint(g, x0, y0, zoom);

        int xApp = (int) (x0 + x * zoom);
        int yApp = (int) (g.getClipBounds().height - (y0 + y * zoom));
        g.setColor(Color.black);
        String text = "" + this.getTension();
        if (!text.contains("E")) {
            text = text.substring(0, Math.min(6, text.length()));
        }
        g.drawString(text, xApp, yApp);
    }

}
