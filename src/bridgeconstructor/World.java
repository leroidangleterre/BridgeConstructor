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

    public World() {
        bridge = new Bridge();

    }

    public void paint(Graphics g, double x0, double y0, double zoom) {
        System.out.println("World.paint");
        bridge.paint(g, x0, y0, zoom);
        paintCenter(g, x0, y0, zoom);
    }

    private void paintCenter(Graphics g, double x0, double y0, double zoom) {
        g.setColor(Color.black);
        int h = g.getClipBounds().height;
        g.drawLine((int) x0, (int) (h - y0), (int) (x0 + zoom), (int) (h - y0));
        g.drawLine((int) x0, (int) (h - y0), (int) x0, (int) (h - (y0 + zoom)));
    }

}
