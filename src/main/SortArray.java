package main;

import algorithms.ISortAlgorithm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SortArray extends JPanel {

    public static final int DEFAULT_WIN_WIDTH = 1280;
    public static final int DEFAULT_WIN_HEIGHT = 720;
    private static final int DEFAULT_BAR_WIDTH = 5;

    private static final double BAR_HEIGHT_PERCENT = 512.0 / 720.0;
    private static final int NUM_BARS = DEFAULT_WIN_WIDTH / DEFAULT_BAR_WIDTH;

    private final int[] array;
    private final int[] barColours;
    private String algorithmName = "";
    private ISortAlgorithm algorithm;
    private long algorithmDelay = 0;

    private MidiSoundPlayer player;
    private JSpinner spinner;
    private JSlider slider;
    private boolean playSound;

    //Number of changes to the array
    private int arrayChanges = 0;

    public SortArray(boolean playSound) {
        this.playSound = playSound;

        setBackground(Color.DARK_GRAY);
        array = new int[NUM_BARS];
        barColours = new int[NUM_BARS];
        for (int i = 0; i < NUM_BARS; i++) {
            array[i] = i;
            barColours[i] = 0;
        }

        player = new MidiSoundPlayer(NUM_BARS);

        //Add spinner
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        spinner.addChangeListener(event -> {
            algorithmDelay = (Integer) spinner.getValue();
            algorithm.setDelay(algorithmDelay);
            slider.setValue((Integer) spinner.getValue());
        });
        add(spinner, BorderLayout.LINE_START);

        //Add slider
        slider = new JSlider(1, 500);
        slider.setPaintTicks(true);
        slider.setPaintTrack(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                algorithmDelay = (Integer) slider.getValue();
                algorithm.setDelay(algorithmDelay);
                spinner.setValue((Integer) slider.getValue());
            }
        });
        add(slider, BorderLayout.LINE_START);
    }

    public int arraySize() {
        return array.length;
    }

    public int getValue(int index) {
        return array[index];
    }

    public int getMaxValue() {
        return Arrays.stream(array)
                .max()
                .orElse(Integer.MIN_VALUE);
    }

    public int getMinValue() {
        return Arrays.stream(array)
                .min()
                .orElse(Integer.MAX_VALUE);
    }

    public void swap(int firstIndex, int secondIndex, long millisecondDelay, boolean isStep) {
        int temp = array[firstIndex];
        array[firstIndex] = array[secondIndex];
        array[secondIndex] = temp;

        barColours[firstIndex] = 100;
        barColours[secondIndex] = 100;

        finaliseUpdate((array[firstIndex] + array[secondIndex]) / 2, millisecondDelay, isStep);
    }

    private void finaliseUpdate(int value, long millisecondDelay, boolean isStep) {
        repaint();

        try {
            Thread.sleep(millisecondDelay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (playSound)
            player.makeSound(value);

        if (isStep)
            arrayChanges++;
    }

    public void updateSingle(int index, int value, long millisecondDelay, boolean isStep) {
        array[index] = value;
        barColours[index] = 100;

        finaliseUpdate(value, millisecondDelay, isStep);
        repaint();
    }

    public void shuffle() {
        arrayChanges = 0;
        Random random = new Random();
        for (int i = 0; i < arraySize(); i++) {
            int swapWithIndex = random.nextInt(arraySize() - 1);
            swap(i, swapWithIndex, 5, false);
        }
        arrayChanges = 0;
    }

    public void highlightArray() {
        for (int i = 0; i < arraySize(); i++)
            updateSingle(i, getValue(i), 5, false);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIN_WIDTH, DEFAULT_WIN_HEIGHT);
    }

    public void resetColours() {
        for (int i = 0; i < NUM_BARS; i++) barColours[i] = 0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g.create();

        try {
            Map<RenderingHints.Key, Object> renderingHints = new HashMap<>();
            renderingHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.addRenderingHints(renderingHints);
            graphics.setColor(Color.white);
            graphics.setFont(new Font("Monospaced", Font.BOLD, 20));
            graphics.drawString(" Current algorithm: " + algorithmName, 10, 30);
            graphics.drawString("Current step delay: " + algorithmDelay, 10, 55);
            graphics.drawString("     Array Changes: " + arrayChanges, 10, 80);
            graphics.drawString("        Array Size: " + arraySize(), 10, 105);

            drawBars(graphics);
        } finally {
            graphics.dispose();
        }
    }

    private void drawBars(Graphics2D graphics) {
        int barWidth = getWidth() / NUM_BARS;
        int bufferedImageWidth = barWidth * NUM_BARS;
        int bufferedImageHeight = getHeight();

        if (bufferedImageHeight > 0 && bufferedImageWidth > 0) {
            bufferedImageWidth = Math.max(bufferedImageWidth, 256);

            double maxValue = getMaxValue();

            BufferedImage bufferedImage = new BufferedImage(bufferedImageWidth, bufferedImageHeight, BufferedImage.TYPE_INT_ARGB);
            makeBufferedImageTransparent(bufferedImage);
            Graphics2D bufferedGraphics = null;
            try {
                bufferedGraphics = bufferedImage.createGraphics();

                for (int bar = 0; bar < NUM_BARS; bar++) {
                    double currentValue = getValue(bar);
                    double percentOfMax = currentValue / maxValue;
                    double heightPercentOfPanel = percentOfMax * BAR_HEIGHT_PERCENT;
                    int height = (int) (heightPercentOfPanel * (double) getHeight());
                    int xBegin = bar + (barWidth - 1) * bar;
                    int yBegin = getHeight() - height;

                    int value = barColours[bar] * 2;
                    if (value > 190)
                        bufferedGraphics.setColor(new Color(255 - value, 255, 255 - value));
                    else
                        bufferedGraphics.setColor(new Color(255, 255 - value, 255 - value));
                    bufferedGraphics.fillRect(xBegin, yBegin, barWidth, height);
                    if (barColours[bar] > 0)
                        barColours[bar] -= 5;
                }
            } finally {
                if (bufferedGraphics != null)
                    bufferedGraphics.dispose();
            }

            graphics.drawImage(bufferedImage,0, 0, getWidth(), getHeight(), 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        }
    }

    private void makeBufferedImageTransparent(BufferedImage bufferedImage) {
        Graphics2D bufferedGraphics = null;
        try {
            bufferedGraphics = bufferedImage.createGraphics();

            bufferedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
            bufferedGraphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            bufferedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        } finally {
            if (bufferedGraphics != null)
                bufferedGraphics.dispose();
        }
    }

    @Override
    public void setName(String name) {
        this.algorithmName = name;
    }

    public void setAlgorithm(ISortAlgorithm algorithm) {
        this.algorithm = algorithm;
        algorithmDelay = algorithm.getDelay();
        //Set value
        spinner.setValue((int) algorithm.getDelay());
        slider.setValue((int) algorithm.getDelay());
    }

    public long getAlgorithmDelay() {
        return algorithmDelay;
    }
}
