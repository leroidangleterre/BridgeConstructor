package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arthu
 */
public class Bridge {

    private ArrayList<BridgeElement> bridgeElements;

    private BridgeElement newElement;
    // The coordinates of the first end of the new plank
    private double xStart, yStart;

    private int gridSize; // The number of columns of dots displayed
    private double gridStep; // The distance between two dots of the same line or column

    public Bridge() {
        bridgeElements = new ArrayList<>();
        gridSize = 8;
        gridStep = 5;
    }

    public void paint(Graphics g, double x0, double y0, double zoom) {

        paintGrid(g, x0, y0, zoom);

        for (BridgeElement p : bridgeElements) {
            p.paint(g, x0, y0, zoom);
        }
        if (newElement != null) {
            newElement.paint(g, x0, y0, zoom);
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
        if (newElement != null) {
            // The first end of the plank or cable already is at (xStart, yStart);
            // the second end goes to (x, y)
            newElement.setEndCoords(xStart, yStart,
                    snapToGrid(x), snapToGrid(y));
        }
    }

    void createPlank(double x, double y) {

        xStart = snapToGrid(x);
        yStart = snapToGrid(y);

        newElement = new Plank(xStart, yStart, 0, 0);
    }

    void createCable(double x, double y) {

        xStart = snapToGrid(x);
        yStart = snapToGrid(y);

        newElement = new Cable(xStart, yStart, 0, 0);
    }

    void createConcrete(double x, double y) {

        xStart = snapToGrid(x);
        yStart = snapToGrid(y);

        newElement = new Concrete(xStart, yStart, 0, 0);
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
    void finishNewElement() {
        newElement.computeMassAndInertiaMoment();
        if (newElement.length != 0) {
            bridgeElements.add(newElement);
        }
        newElement = null;
        rebuildSprings();
    }

    /**
     * Delete the object (plank, cable, ...) that is closest to the given
     * coordinates.
     *
     * @param x
     * @param y
     */
    void deleteClosest(double x, double y) {

        BridgeElement closestElement = findClosest(x, y);
        bridgeElements.remove(closestElement);
    }

    /**
     * Find the plank that is closest to the given coordinates.
     *
     * @param x
     * @param y
     * @return
     */
    private BridgeElement findClosest(double x, double y) {
        BridgeElement closestYet = null;
        double shortestDistanceYet = Double.MAX_VALUE;

        for (BridgeElement p : bridgeElements) {
            double distance = p.getDistance(x, y);
            if (distance < shortestDistanceYet) {
                shortestDistanceYet = distance;
                closestYet = p;
            }
        }
        return closestYet;
    }

    void step(double dt) {

        // Part one: compute the force and momentum applied on each element;
        // Change the velocity and rotation rate
        computeAllForcesAndMomentums(dt);

        // Part two: dampen the speeds
        dampenSpeeds(dt);

        // Part three: update the position and rotation
        updatePositionsAndRotations(dt);

        updateStringLengths();
    }

    private void computeAllForcesAndMomentums(double dt) {
        for (BridgeElement be : bridgeElements) {
            be.resetTension();
        }
        for (BridgeElement be : bridgeElements) {
            if (be instanceof Spring) {
                ((Spring) be).applyForce(dt);
            }
        }
    }

    /**
     * When all planks, cables, and other elements are set, and before the
     * simulation starts,
     * we must create the springs that unite them.
     */
    private void rebuildSprings() {

        deleteAllSprings();
        buildSprings();
    }

    private void deleteAllSprings() {

        for (int rank = bridgeElements.size() - 1; rank >= 0; rank--) {
            if (bridgeElements.get(rank) instanceof Spring) {
                bridgeElements.remove(rank);
            }
        }
    }

    private void buildSprings() {

        int rank0 = 0;
        while (rank0 < bridgeElements.size() && !(bridgeElements.get(rank0) instanceof Spring)) {
            BridgeElement be0 = bridgeElements.get(rank0);
            int rank1 = rank0 + 1;
            while (rank1 < bridgeElements.size() && !(bridgeElements.get(rank1) instanceof Spring)) {
                BridgeElement be1 = bridgeElements.get(rank1);
                tryBuildSpring(be0, be1);
                rank1++;
            }
            rank0++;
        }
    }

    private void tryBuildSpring(BridgeElement be0, BridgeElement be1) {

        Spring newSpring = null;

        // See if the pair (be0, be1) has a point in common.
        // First possibility: it is the first point for both objects
        if (be0.sharePoint(be1, true, true)) {
            newSpring = new Spring();
            newSpring.setTargets(be0, true, be1, true);
        }
        // Second possibility: first point for be0, second for be1
        if (be0.sharePoint(be1, true, false)) {
            newSpring = new Spring();
            newSpring.setTargets(be0, true, be1, false);
        }
        // Third possibility: second point for be0, first for be1
        if (be0.sharePoint(be1, false, true)) {
            newSpring = new Spring();
            newSpring.setTargets(be0, false, be1, true);
        }
        // Fourth possibility: second point for both
        if (be0.sharePoint(be1, false, false)) {
            newSpring = new Spring();
            newSpring.setTargets(be0, false, be1, false);
        }

        if (newSpring != null) {
            // We found a point in common, and created a spring.
            bridgeElements.add(newSpring);
        }
    }

    private void updatePositionsAndRotations(double dt) {
        for (BridgeElement be : bridgeElements) {
            be.moveAndRotateAtCurrentSpeed(dt);
        }
    }

    /**
     * Apply gravity to the elements of the bridge
     *
     * @param gy
     * @param dt
     */
    void applyGravity(double gy, double dt) {
        for (BridgeElement be : bridgeElements) {
            be.changeVelocity(0, gy, dt);
        }
    }

    private void updateStringLengths() {
        for (BridgeElement be : bridgeElements) {
            if (be instanceof Spring) {
                ((Spring) be).updateLength();
            }
        }
    }

    private void dampenSpeeds(double dt) {
        for (BridgeElement be : bridgeElements) {
            be.dampenSpeed(dt);
        }
    }

    void stopMovement() {
        for (BridgeElement be : bridgeElements) {
            be.stopMovement();
        }
    }

    void restart() {
        for (BridgeElement be : bridgeElements) {
            be.restart();
        }
        rebuildSprings();
    }

    private double getMinTension() {
        double minTension = Double.MAX_VALUE;
        for (BridgeElement be : bridgeElements) {
            if (be.getTension() < minTension) {
                minTension = be.getTension();
            }
        }
        return minTension;
    }

    private double getMaxTension() {
        double maxTension = Double.MIN_VALUE;
        for (BridgeElement be : bridgeElements) {
            if (be.getTension() > maxTension) {
                maxTension = be.getTension();
            }
        }
        return maxTension;
    }

    /**
     * Save the bridge to a file.
     *
     * @param writer the BufferedWriter used to open the file.
     */
    void save(BufferedWriter writer) {

        for (BridgeElement be : bridgeElements) {
            be.save(writer);
        }
    }

    /**
     * Load a bridge from a file.
     *
     * @param reader the BufferedReader used to open the file.
     */
    void load(BufferedReader reader) {
        bridgeElements.clear();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String tab[] = line.split(" ");
                String type = tab[1].split("\\.")[1];
                double x = Double.valueOf(tab[2]);
                double y = Double.valueOf(tab[3]);
                double angle = Double.valueOf(tab[4]);
                double length = Double.valueOf(tab[5]);

                // Create a new BridgeElement
                newElement = null;
                switch (type) {
                case "Plank":
                    newElement = new Plank(x, y, angle, length);
                    break;
                case "Concrete":
                    newElement = new Concrete(x, y, angle, length);
                    break;
                case "Cable":
                    newElement = new Cable(x, y, angle, length);
                    break;
                default:
                    break;
                }
                if (newElement != null) {

                    newElement.setEndCoords(
                            newElement.getXA(),
                            newElement.getYA(),
                            newElement.getXB(),
                            newElement.getYB());
                    newElement.computeMassAndInertiaMoment();

                    bridgeElements.add(newElement);
                }
            }
            buildSprings();
        } catch (IOException ex) {
            Logger.getLogger(Bridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
