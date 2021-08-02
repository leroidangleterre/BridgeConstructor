package bridgeconstructor;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author arthu
 */
public class Bridge {

    private ArrayList<Plank> planks;

    public Bridge() {
        planks = new ArrayList<>();
        buildBasicBridge();
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {
        for (Plank p : planks) {
            p.paint(g, x0, y0, zoom);
        }
    }

    private void buildBasicBridge() {

        int nbPlanks = 5;
        double radius = 10;

        double y0 = 100;

        for (int i = 0; i <= nbPlanks; i++) {
            double angle = Math.PI * (i + 1 / 2) / nbPlanks;
            double x = radius * Math.cos(angle);
            double y = y0 + radius * Math.sin(angle);
            double length = radius * Math.PI / nbPlanks;
            Plank p = new Plank(x, y, angle, length);
            planks.add(p);
        }
    }

}
