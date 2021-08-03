package bridgeconstructor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author arthu
 */
public class BridgeMenu extends JPanel {

    private final JButton plankButton;
    private final JButton cableButton;
    private final JButton concreteButton;
    private final JButton stepButton;
    private final JButton playPauseButton;
    private final JButton deleteButton;

    public BridgeMenu(GraphicPanel panel) {

        super();

        plankButton = new JButton("Plank");
        plankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click new plank");
                panel.setMode(UIMode.PLANK_CREATION);
            }
        });
        this.add(plankButton);

        cableButton = new JButton("Cable");
        cableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click new cable");
                panel.setMode(UIMode.CABLE_CREATION);
            }
        });
        this.add(cableButton);

        concreteButton = new JButton("Concrete");
        concreteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click new concrete");
                panel.setMode(UIMode.CONCRETE_CREATION);
            }
        });
        this.add(concreteButton);

        playPauseButton = new JButton("Play");
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click play");
            }
        });
        this.add(playPauseButton);

        stepButton = new JButton("Step");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("");
                System.out.println("------------------------------------");
                System.out.println("Click step");
                panel.step();
            }
        });
        this.add(stepButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click delete");
                panel.setMode(UIMode.DELETE);
            }
        });
        this.add(deleteButton);
    }

}
