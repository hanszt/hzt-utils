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

    public static void shuffle(LongMutableList list) {
        if (random == null) {
            random = new Random(); // harmless race.
        }
        shuffle(list, random);
    }

    public static void shuffle(DoubleMutableList list) {
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

    public static void shuffle(LongMutableList list, Random random) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--) {
                swap(list, i - 1, random.nextInt(i));
            }
        } else {
            long[] array = list.toArray();
            for (int i = size; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
            for (int i = 0; i < list.size(); i++) {
                list.set(i, array[i]);
            }
        }
    }

    public static void shuffle(DoubleMutableList list, Random random) {
        int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i = size; i > 1; i--) {
                swap(list, i - 1, random.nextInt(i));
            }
        } else {
            double[] array = list.toArray();
            for (int i = size; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
            for (int i = 0; i < list.size(); i++) {
                list.set(i, array[i]);
            }
        }
    }

    public static void swap(IntMutableList list, int i, int j) {
        final var element = list.get(i);
        final var other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(int[] arr, int i, int j) {
        final int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(LongMutableList list, int i, int j) {
        final long element = list.get(i);
        final long other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(long[] arr, int i, int j) {
        final long tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(DoubleMutableList list, int i, int j) {
        final double element = list.get(i);
        final double other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(double[] arr, int i, int j) {
        double tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
