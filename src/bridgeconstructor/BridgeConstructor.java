package bridgeconstructor;

import javax.swing.JFrame;

/**
 *
 * @author arthu
 */
public class BridgeConstructor {

    public static void main(String[] args) {

        int width = 800;
        int height = 400;

        JFrame window = new JFrame();
        World world = new World();
        GraphicPanel panel = new GraphicPanel(world);
        window.setSize(width, height);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(panel);
        window.setVisible(true);
    }

}
