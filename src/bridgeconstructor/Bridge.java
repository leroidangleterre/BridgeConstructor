package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author arthu
 */
public class Bridge {

    private ArrayList<Plank> planks;

    private Plank newPlank;
    // The coordinates of the first end of the new plank
    private double xPlankStart, yPlankStart;

    private int gridSize; // The number of columns of dots displayed
    private double gridStep; // The distance between two dots of the same line or column

    public Bridge() {
        planks = new ArrayList<>();
        gridSize = 8;
        gridStep = 5;
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {

        paintGrid(g, x0, y0, zoom);

        for (Plank p : planks) {
            p.paint(g, x0, y0, zoom);
        }
        if (newPlank != null) {
            newPlank.paint(g, x0, y0, zoom);
        }
    }

    private void paintGrid(Graphics g, double x0, double y0, double zoom) {

        g.setColor(Color.red);
        int radius = 2;

        int h = g.getClipBounds().height;

        for (int i = -gridSize; i <= gridSize; i++) {
            int yApp = (int) (h - (y0 + (gridStep * i) * zoom));
            for (int j = -gridSize; j <= gridSize; j++) {
                int xApp = (int) (x0 + (gridStep * j) * zoom);
                g.fillOval(xApp - radius, yApp - radius, 2 * radius, 2 * radius);
            }
        }
    }

    void mouseDragged(double x, double y) {
        if (newPlank != null) {
            // The second end of the plank goes to (x, y)
            newPlank.setEndCoords(xPlankStart, yPlankStart,
                    snapToGrid(x), snapToGrid(y));
        }
    }

    void createPlank(double x, double y) {

        xPlankStart = snapToGrid(x);
        yPlankStart = snapToGrid(y);

        newPlank = new Plank(xPlankStart, yPlankStart, 0, 0);
    }

    /**
     * Return the value of the grid that is closest to the given value.
     */
    private double snapToGrid(double x) {
        return (Math.floor(x / gridStep + 0.5)) * gridStep;
    }

    /**
     * The plank that is being built must now be added to the list
     * and not modified anymore.
     */
    void finishNewPlank() {
        planks.add(newPlank);
        newPlank = null;
    }

    /**
     * Delete the object (plank, cable, ...) that is closest to the given
     * coordinates.
     *
     * @param x
     * @param y
     */
    void deleteClosest(double x, double y) {

        Plank closestPlank = findClosest(x, y);
        planks.remove(closestPlank);
    }

    /**
     * Find the plank that is closest to the given coordinates.
     *
     * @param x
     * @param y
     * @return
     */
    private Plank findClosest(double x, double y) {
        Plank closestYet = null;
        double shortestDistanceYet = Double.MAX_VALUE;

        for (Plank p : planks) {
            double distance = p.getDistance(x, y);
            if (distance < shortestDistanceYet) {
                shortestDistanceYet = distance;
                closestYet = p;
            }
        }
        return closestYet;
    }
}
