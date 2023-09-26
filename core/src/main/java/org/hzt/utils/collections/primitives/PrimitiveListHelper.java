package org.hzt.utils.collections.primitives;

import java.util.Random;
import java.util.RandomAccess;

final class PrimitiveListHelper {

    private static final int SHUFFLE_THRESHOLD = 5;

    private static Random random = null;

    private PrimitiveListHelper() {
    }

    public static void shuffle(final IntMutableList list) {
        if (random == null) {
            random = new Random(); // harmless race.
        }
        shuffle(list, random);
    }

    public static void shuffle(final LongMutableList list) {
        if (random == null) {
            random = new Random(); // harmless race.
        }
        shuffle(list, random);
    }

    public static void shuffle(final DoubleMutableList list) {
        if (random == null) {
            random = new Random(); // harmless race.
        }
        shuffle(list, random);
    }

    public static void shuffle(final IntMutableList list, final Random random) {
        final var size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (var i = size; i > 1; i--) {
                swap(list, i - 1, random.nextInt(i));
            }
        } else {
            final var array = list.toArray();
            for (var i = size; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
            for (var i = 0; i < list.size(); i++) {
                list.set(i, array[i]);
            }
        }
    }

    public static void shuffle(final LongMutableList list, final Random random) {
        final var size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (var i = size; i > 1; i--) {
                swap(list, i - 1, random.nextInt(i));
            }
        } else {
            final var array = list.toArray();
            for (var i = size; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
            for (var i = 0; i < list.size(); i++) {
                list.set(i, array[i]);
            }
        }
    }

    public static void shuffle(final DoubleMutableList list, final Random random) {
        final var size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (var i = size; i > 1; i--) {
                swap(list, i - 1, random.nextInt(i));
            }
        } else {
            final var array = list.toArray();
            for (var i = size; i > 1; i--) {
                swap(array, i - 1, random.nextInt(i));
            }
            for (var i = 0; i < list.size(); i++) {
                list.set(i, array[i]);
            }
        }
    }

    public static void swap(final IntMutableList list, final int i, final int j) {
        final var element = list.get(i);
        final var other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(final int[] arr, final int i, final int j) {
        final var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(final LongMutableList list, final int i, final int j) {
        final var element = list.get(i);
        final var other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(final long[] arr, final int i, final int j) {
        final var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void swap(final DoubleMutableList list, final int i, final int j) {
        final var element = list.get(i);
        final var other = list.set(j, element);
        list.set(i, other);
    }

    private static void swap(final double[] arr, final int i, final int j) {
        final var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
