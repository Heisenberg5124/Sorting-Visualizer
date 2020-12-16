package screens;

import algorithms.ISortAlgorithm;
import main.MainApp;
import main.SortArray;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public final class SortingVisualiserScreen extends Screen {

    private final SortArray sortArray;
    private final ArrayList<ISortAlgorithm> algorithmList;

    public SortingVisualiserScreen(MainApp app, ArrayList<ISortAlgorithm> algorithms, boolean playSounds) {
        super(app);
        setLayout(new BorderLayout());
        sortArray = new SortArray(playSounds);
        add(sortArray, BorderLayout.CENTER);
        algorithmList = algorithms;
    }

    @Override
    public void onOpen() {
        SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (ISortAlgorithm algorithm : algorithmList) {
                    shuffleAndWait();

                    sortArray.setName(algorithm.getName());
                    sortArray.setAlgorithm(algorithm);

                    algorithm.runSort(sortArray);
                    sortArray.resetColours();
                    sortArray.highlightArray();
                    sortArray.resetColours();
                    longSleep();
                }
                return null;
            }

            @Override
            protected void done() {
                app.popScreen();
            }
        };

        swingWorker.execute();
    }

    private void shuffleAndWait() {
        sortArray.shuffle();
        sortArray.resetColours();
        longSleep();
    }

    private void longSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
