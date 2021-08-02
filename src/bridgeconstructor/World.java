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
//            bridge.createCable(xWorld, yWorld);
            break;
        default:
            throw new AssertionError(currentMode.name());

        }
    }

    protected void setMode(UIMode newMode) {
        currentMode = newMode;
    }

    void mouseReleased() {
        switch (currentMode) {
        case PLANK_CREATION:
            bridge.finishNewPlank();
        }
    }

    void mouseDragged(double x, double y) {
        bridge.mouseDragged(x, y);
    }
}
