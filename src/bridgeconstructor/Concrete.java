package bridgeconstructor;

import java.awt.Color;

/**
 *
 * @author arthu
 */
public class Concrete extends BridgeElement {

    public Concrete(double newX, double newY, double newAngle, double newLength) {
        super(newX, newY, newAngle, newLength);
        lineicMass = 100;
    }

    @Override
    public Color getColor() {
        return Color.black; // Color does not depend on constraints, material is unbreakable.
    }

    /**
     * The concrete may receive any force at all without moving.
     *
     * @param fx
     * @param fy
     * @param dt
     */
    @Override
    void receiveForce(double fx, double fy,
            double xApplication, double yApplication,
            double dt) {
        // Just do nothing.
    }

    @Override
    void changeVelocity(double fx, double fy, double dt) {
        // Just do not change the velocity
    }

    @Override
    void computeMassAndInertiaMoment() {
        // Unused
    }

}
