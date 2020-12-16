package algorithms;

import main.SortArray;

public class BubbleSort implements ISortAlgorithm {

    private long stepDelay = 2;

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public long getDelay() {
        return stepDelay;
    }

    @Override
    public void setDelay(long delay) {
        this.stepDelay = delay;
    }

    @Override
    public void runSort(SortArray array) {
        int length = array.arraySize();

        for (int i = 0; i < length - 1; i++)
            for (int j = 0; j < length - 1 - i; j++)
                if (array.getValue(j) > array.getValue(j + 1))
                    array.swap(j, j + 1, getDelay(), true);
    }
}
