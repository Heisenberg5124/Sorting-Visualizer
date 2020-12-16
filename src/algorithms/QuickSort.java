package algorithms;

import main.SortArray;

public class QuickSort implements ISortAlgorithm {

    private long stepDelay = 100;

    @Override
    public String getName() {
        return "Quick Sort";
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
        quickSort(array, 0, array.arraySize() - 1);
    }

    private void quickSort(SortArray array, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int pivot = findPivot(array, lowIndex, highIndex);
            quickSort(array, lowIndex, pivot - 1);
            quickSort(array, pivot + 1, highIndex);
        }
    }

    private int findPivot(SortArray array, int lowIndex, int highIndex) {
        int pivotValue = array.getValue(highIndex);
        int i = lowIndex - 1;
        for (int j = lowIndex; j < highIndex; j++)
            if (array.getValue(j) <= pivotValue)
                array.swap(++i, j, getDelay(), true);

        array.swap(i + 1, highIndex, getDelay(), true);
        return i + 1;
    }
}
