package screens;

import algorithms.*;
import main.MainApp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class MainMenuScreen extends Screen {

    private static final Color BACKGROUND_COULOUR = Color.DARK_GRAY;
    private final ArrayList<AlgorithmCheckBox> checkBoxes;

    private static final String LOGO_FILE_NAME = "src/resources/logo.png";

    public MainMenuScreen(MainApp app) {
        super(app);
        checkBoxes = new ArrayList<>();
        setUpGUI();
    }

    private void setUpGUI() {
        JPanel sortAlgorithmContainer = new JPanel();
        JPanel optionsContainer = new JPanel();
        JPanel outerContainer = new JPanel();
        JLabel label;
        initContainer(this);
        initContainer(optionsContainer);
        initContainer(sortAlgorithmContainer);

        outerContainer.setBackground(BACKGROUND_COULOUR);
        outerContainer.setLayout(new BoxLayout(outerContainer, BoxLayout.LINE_AXIS));

        //Set logo
        /*ClassLoader loader = getClass().getClassLoader();
        BufferedImage image = ImageIO.read(new File(loader.getResource(LOGO_FILE_NAME).getFile()));*/
        label = new JLabel(new ImageIcon(LOGO_FILE_NAME));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);

        //Add check box of algorithms
        sortAlgorithmContainer.setAlignmentX(CENTER_ALIGNMENT);
        addCheckBox(new BubbleSort(), sortAlgorithmContainer);
        addCheckBox(new QuickSort(), sortAlgorithmContainer);
        addCheckBox(new InsertionSort(), sortAlgorithmContainer);
        addCheckBox(new CountingSort(), sortAlgorithmContainer);
        addCheckBox(new MergeSort(), sortAlgorithmContainer);
        addCheckBox(new HeapSort(), sortAlgorithmContainer);
        addCheckBox(new RadixSort(), sortAlgorithmContainer);
        addCheckBox(new SelectionSort(), sortAlgorithmContainer);

        JCheckBox soundCheckBox = new JCheckBox("Play Sounds");
        soundCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        soundCheckBox.setBackground(BACKGROUND_COULOUR);
        soundCheckBox.setForeground(Color.WHITE);

        optionsContainer.add(soundCheckBox);

        JButton startButton = new JButton("Begin Visual Sorter");
        startButton.addActionListener((ActionEvent e) -> {
            ArrayList<ISortAlgorithm> algorithms = new ArrayList<>();
            for (AlgorithmCheckBox checkBox : checkBoxes)
                if (checkBox.isSelected())
                    algorithms.add(checkBox.getAlgorithm());
            app.pushScreen(new SortingVisualiserScreen(app, algorithms, soundCheckBox.isSelected()));
        });
        startButton.setAlignmentX(CENTER_ALIGNMENT);

        outerContainer.add(optionsContainer);
        outerContainer.add(Box.createRigidArea(new Dimension(5, 0)));
        outerContainer.add(sortAlgorithmContainer);

        int gap = 15;
        add(Box.createRigidArea(new Dimension(0, gap)));
        add(outerContainer);
        add(Box.createRigidArea(new Dimension(0, gap)));
        add(startButton);
    }

    private void addCheckBox(ISortAlgorithm algorithm, JPanel panel) {
        JCheckBox checkBox = new JCheckBox("", true);
        checkBox.setAlignmentX(LEFT_ALIGNMENT);
        checkBox.setBackground(BACKGROUND_COULOUR);
        checkBox.setForeground(Color.WHITE);
        checkBoxes.add(new AlgorithmCheckBox(algorithm, checkBox));
        panel.add(checkBox);
    }

    private void initContainer(JPanel panel) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBackground(BACKGROUND_COULOUR);
    }

    @Override
    public void onOpen() {
        checkBoxes.forEach((AlgorithmCheckBox::unselect));
    }

    private class AlgorithmCheckBox {
        private final ISortAlgorithm algorithm;
        private final JCheckBox checkBox;

        public AlgorithmCheckBox(ISortAlgorithm algorithm, JCheckBox checkBox) {
            this.algorithm = algorithm;
            this.checkBox = checkBox;
            this.checkBox.setText(algorithm.getName());
        }

        public void unselect() {
            checkBox.setSelected(false);
        }

        public boolean isSelected() {
            return checkBox.isSelected();
        }

        public ISortAlgorithm getAlgorithm() {
            return algorithm;
        }
    }
}
