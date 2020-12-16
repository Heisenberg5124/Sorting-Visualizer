package algorithms;

import main.SortArray;

import java.util.Arrays;

public class RadixSort implements ISortAlgorithm {

    private long stepDelay = 5;
    private int radix;
    private int[] countingArray;

    public RadixSort(int radix) {
        this.radix = radix;
        countingArray = new int[radix];
    }

    public RadixSort() {
        this(10);
    }

    @Override
    public String getName() {
        return "Radix Sort";
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
        int maxValue = array.getMaxValue();
        int[] result = new int[array.arraySize()];

        for (int exp = 1; maxValue / exp > 0; exp *= radix) {
            countingArray = countingSort(array, exp);

            for (int i = 0; i < result.length; i++)
                array.updateSingle(i, result[i] = array.getValue(i), getDelay(), false);

            for (int i = 1; i < radix; i++)
                countingArray[i] += countingArray[i - 1];

            for (int i = array.arraySize() - 1; i > -1; i--)
                array.updateSingle(--countingArray[(result[i] / exp) % radix], result[i], getDelay(), true);
        }
    }

    private int[] countingSort(SortArray array, int exp) {
        Arrays.fill(countingArray, 0);
        for (int i = 0; i < array.arraySize(); i++)
            countingArray[(array.getValue(i) / exp) % radix]++;
        return countingArray;
    }
}
