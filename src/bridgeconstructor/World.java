package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author arthu
 */
public class World {

    // Non-moving objects (ground, mountains, river bed...)
    // Bridge
    private Bridge bridge;

    public static double gravity = -1;

    // Vehicles travelling somewhere on the ground or on the bridge (or falling from the bridge)
    private ArrayList<Vehicle> vehicles;

    private UIMode currentMode;

    public World() {
        bridge = new Bridge();

        currentMode = UIMode.PLANK_CREATION;
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {
        bridge.paint(g, x0, y0, zoom);
        paintCenter(g, x0, y0, zoom);
    }

    private void paintCenter(Graphics g, double x0, double y0, double zoom) {
        g.setColor(Color.black);
        int h = g.getClipBounds().height;
        g.drawLine((int) x0, (int) (h - y0), (int) (x0 + zoom), (int) (h - y0));
        g.drawLine((int) x0, (int) (h - y0), (int) x0, (int) (h - (y0 + zoom)));
    }

    protected void mousePressed(double xWorld, double yWorld) {

        switch (currentMode) {
        case PLANK_CREATION:
            bridge.createPlank(xWorld, yWorld);
            break;
        case CABLE_CREATION:
            bridge.createCable(xWorld, yWorld);
            break;
        case CONCRETE_CREATION:
            bridge.createConcrete(xWorld, yWorld);
            break;
        case DELETE:
            // Must delete the closest plank or cable
            bridge.deleteClosest(xWorld, yWorld);
            break;
        default:
        }
    }

    protected void setMode(UIMode newMode) {
        currentMode = newMode;
    }

    void mouseReleased() {
        switch (currentMode) {
        case PLANK_CREATION:
        case CABLE_CREATION:
        case CONCRETE_CREATION:
            bridge.finishNewElement();
        }
    }

    void mouseDragged(double x, double y) {
        bridge.mouseDragged(x, y);
    }

    /**
     * Compute one step of evolution
     */
    public void step() {
        this.step(0.1);
    }

    public void step(double dt) {
        bridge.step(dt);
        applyGravity(this.gravity, dt);
    }

    /**
     * Apply gravity to the bridge, the vehicles, and any other object.
     *
     * @param dt
     */
    private void applyGravity(double g, double dt) {
        bridge.applyGravity(g, dt);
    }

}
