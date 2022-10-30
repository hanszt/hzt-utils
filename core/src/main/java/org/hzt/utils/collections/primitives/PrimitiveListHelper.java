package org.hzt.utils.collections.primitives;

import java.util.Random;
import java.util.RandomAccess;

final class PrimitiveListHelper {

    private static final int SHUFFLE_THRESHOLD = 5;

    private static Random random = null;

    private PrimitiveListHelper() {
    }

    public static void shuffle(IntMutableList list) {
        if (random == null) {
            random = new Random(); // harmless race.
        }
        shuffle(list, random);
    }

    public static void shuffle(IntMutableList list, Random random) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--) {
                swap(list, i - 1, random.nextInt(i));
            }
        } else {
            int[] array = list.toArray();
            for (int i = size; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
            for (int i = 0; i < list.size(); i++) {
                list.set(i, array[i]);
            }
        }
    }

    public static void swap(IntMutableList list, int i, int j) {
        final int element = list.get(i);
        final int other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    static int checkIndex(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        return index;
    }
}
