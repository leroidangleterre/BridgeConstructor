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

    private World w;

    private double x0, y0, zoom;
    private int xMouse, yMouse;

    public GraphicPanel(World world) {
        super();
        w = world;
        x0 = 100;
        y0 = 0;
        zoom = 1;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        w.paint(g, x0, y0, zoom);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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

        x0 += dx;
        y0 -= dy;
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
        case 0:
            // touchpad
            break;
        default:
            break;
        }

        int h = this.getHeight();

        x0 = zoomFact * (x0 - e.getX()) + e.getX();
        y0 = h - e.getY() - zoomFact * (h - y0 - e.getY());
        zoom = zoom * zoomFact;

        repaint();
    }
}
