package bridgeconstructor;

import java.awt.Color;

/**
 *
 * @author arthu
 */
public class Cable extends BridgeElement {

    public Cable(double newX, double newY, double newAngle, double newLength) {
        super(newX, newY, newAngle, newLength);

        lineicMass = 10;
    }

    @Override
    void computeMassAndInertiaMoment() {
        this.mass = lineicMass * length;
        this.inertiaMoment = (1 / 12) * mass * length * length;
    }

    @Override
    public Color getColor() {
        return Color.gray; // Must be replaced by a color ramp based on tension.
    }

    @Override
    void dampenSpeed(double dt) {
        System.out.println("TODO cable dampenSpeed");
    }

}
