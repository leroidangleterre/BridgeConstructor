package bridgeconstructor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

/**
 *
 * @author arthu
 */
public class GraphicPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private World world;

    private double x0, y0, zoom;
    private int xMouse, yMouse;

    private boolean isPanning;

    public GraphicPanel(World newWorld) {
        super();
        world = newWorld;
        x0 = 550.5;
        y0 = 363.7;
        zoom = 9.85;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        isPanning = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        world.paint(g, x0, y0, zoom);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            isPanning = false;
            double xWorld = (e.getX() - x0) / zoom;
            double yWorld = (this.getHeight() - e.getY() - y0) / zoom;
            world.mousePressed(xWorld, yWorld);
        } else if (e.getButton() == 2) {
            // Mousewheel click, must pan the view
            isPanning = true;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == 1) {
            world.mouseReleased();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - xMouse;
        int dy = e.getY() - yMouse;
        xMouse = e.getX();
        yMouse = e.getY();

        if (isPanning) {
            x0 += dx;
            y0 -= dy;
        } else {
            double xWorld = (e.getX() - x0) / zoom;
            double yWorld = (this.getHeight() - e.getY() - y0) / zoom;
            world.mouseDragged(xWorld, yWorld);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        xMouse = e.getX();
        yMouse = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double zoomFact = 1.0;

        switch (e.getWheelRotation()) {
        case -1:
            // Zoom in
            zoomFact = 1.1;
            break;
        case 1:
            // Zoom out
            zoomFact = 1 / 1.1;
            break;
        default:
            break;
        }

        int h = this.getHeight();

        x0 = zoomFact * (x0 - e.getX()) + e.getX();
        y0 = h - e.getY() - zoomFact * (h - y0 - e.getY());
        zoom = zoom * zoomFact;
        System.out.println("x0: " + x0 + ", y0: " + y0 + ", zoom: " + zoom);
        repaint();
    }

    public void setMode(UIMode uiMode) {
        world.setMode(uiMode);
    }
}
