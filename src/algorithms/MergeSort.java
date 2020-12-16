package algorithms;

import main.SortArray;

public class MergeSort implements ISortAlgorithm {

    private long stepDelay = 20;

    @Override
    public String getName() {
        return "Merge Sort";
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
        int left = 0;
        int right = array.arraySize() - 1;
        mergeSort(array, left, right);
    }

    private void mergeSort(SortArray array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    private void merge(SortArray array, int left, int middle, int right) {
        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        int[] leftArray = getSubArray(array, left, leftSize);
        int[] rightArray = getSubArray(array, middle + 1, rightSize);

        int i = 0, j = 0, k = left;
        while (i < leftSize && j < rightSize)
            if (leftArray[i] <= rightArray[j])
                array.updateSingle(k++, leftArray[i++], getDelay(), true);
            else
                array.updateSingle(k++, rightArray[j++], getDelay(), true);

        while (i < leftSize)
            array.updateSingle(k++, leftArray[i++], getDelay(), true);
        while (j < rightSize)
            array.updateSingle(k++, rightArray[j++], getDelay(), true);
    }

    private int[] getSubArray(SortArray array, int begin, int size) {
        int[] subArray = new int[size];
        for (int i = 0; i < size; i++) subArray[i] = array.getValue(begin + i);
        return subArray;
    }
}
