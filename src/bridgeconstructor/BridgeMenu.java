package bridgeconstructor;

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
    private final JButton restartButton;
    private final JButton deleteButton;
    private final JButton saveButton;
    private final JButton loadButton;

    public BridgeMenu(GraphicPanel panel) {

        super();

        BridgeKeyListener keyListener = new BridgeKeyListener(panel);

        plankButton = new JButton("Plank");
        plankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click new plank");
                panel.setMode(UIMode.PLANK_CREATION);
            }
        });
        plankButton.addKeyListener(keyListener);
        this.add(plankButton);

        cableButton = new JButton("Cable");
        cableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click new cable");
                panel.setMode(UIMode.CABLE_CREATION);
            }
        });
        cableButton.addKeyListener(keyListener);
        this.add(cableButton);

        concreteButton = new JButton("Concrete");
        concreteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click new concrete");
                panel.setMode(UIMode.CONCRETE_CREATION);
            }
        });
        concreteButton.addKeyListener(keyListener);
        this.add(concreteButton);

        playPauseButton = new JButton("Play");
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click play");
                boolean isPlaying = panel.playPause();
                if (isPlaying) {
                    playPauseButton.setText("Pause");
                } else {
                    playPauseButton.setText("Play");
                }
            }
        });
        playPauseButton.addKeyListener(keyListener);
        this.add(playPauseButton);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click restart");
                panel.restart();
            }
        });
        restartButton.addKeyListener(keyListener);
        this.add(restartButton);

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
        stepButton.addKeyListener(keyListener);
        this.add(stepButton);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Click delete");
                panel.setMode(UIMode.DELETE);
            }
        });
        deleteButton.addKeyListener(keyListener);
        this.add(deleteButton);

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.save();
            }
        });
        saveButton.addKeyListener(keyListener);
        this.add(saveButton);

        loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.load();
            }
        });
        loadButton.addKeyListener(keyListener);
        this.add(loadButton);
    }

}
