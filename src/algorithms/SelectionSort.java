package algorithms;

import main.SortArray;

public class SelectionSort implements ISortAlgorithm {

    private long stepDelay = 20;

    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public long getDelay() {
        return stepDelay;
    }

    @Override
    public void setDelay(long delay) {
        stepDelay = delay;
    }

    @Override
    public void runSort(SortArray array) {
        int length = array.arraySize();
        for (int i = 0; i < length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++)
                if (array.getValue(j) < array.getValue(minIndex))
                    minIndex = j;
            array.swap(i, minIndex, getDelay(), true);
        }
    }
}
