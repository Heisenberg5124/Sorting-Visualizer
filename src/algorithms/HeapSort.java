package algorithms;

import main.SortArray;

public class HeapSort implements ISortAlgorithm {

    private long stepDelay = 20;

    @Override
    public String getName() {
        return "Heap Sort";
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
        int size = array.arraySize();
        for (int i = size / 2 - 1; i >= 0; i--)
            toBinaryTree(array, size, i);

        for (int i = size - 1; i >= 0; i--) {
            array.swap(0, i, getDelay(), true);
            toBinaryTree(array, i, 0);
        }
    }

    private void toBinaryTree(SortArray array, int size, int rootIndex) {
        int max = rootIndex;
        int leftChild = 2 * rootIndex + 1;
        int rightChild = 2 * rootIndex + 2;

        if (isChildLargerThanRoot(leftChild, max, size, array))
            max = leftChild;
        if (isChildLargerThanRoot(rightChild, max, size, array))
            max = rightChild;
        if (max != rootIndex) {
            array.swap(rootIndex, max, getDelay(), true);
            toBinaryTree(array, size, max);
        }
    }

    private boolean isChildLargerThanRoot(int child, int max, int size, SortArray array) {
        return child < size && array.getValue(child) > array.getValue(max);
    }
}
