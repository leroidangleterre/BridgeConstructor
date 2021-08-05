package bridgeconstructor;

import colorramp.ColorRamp;
import java.awt.Color;

/**
 *
 * @author arthu
 */
public class Plank extends BridgeElement {

    private colorramp.ColorRamp ramp;

    private double frictionCoef;

    public Plank(double newX, double newY, double newAngle, double newLength) {
        super(newX, newY, newAngle, newLength);
        ramp = new ColorRamp();
        ramp.addValue(0, new Color(167, 116, 73)); // Default brown
        ramp.addValue(1, new Color(185, 71, 0)); // Rust

        lineicMass = 10;

        frictionCoef = 0.0;
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
        return 0;
    }

    @Override
    void dampenSpeed(double dt) {
        this.vx -= vx * frictionCoef / this.mass;
        this.vy -= vy * frictionCoef / this.mass;
    }

}
