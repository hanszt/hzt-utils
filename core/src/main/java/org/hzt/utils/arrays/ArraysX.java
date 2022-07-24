package org.hzt.utils.arrays;

import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public final class ArraysX {

    private ArraysX() {
    }

    public static <T> T[] copyOf(T[] array) {
        return Arrays.copyOf(array, array.length);
    }

    @SafeVarargs
    public static <T> boolean[] toBooleanArray(@NotNull Predicate<? super T> predicate, T @NotNull ... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(@NotNull IntPredicate predicate, int @NotNull ... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(@NotNull LongPredicate predicate, long @NotNull ... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(@NotNull DoublePredicate predicate, double @NotNull ... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static <T> void reverse(T[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void reverse(int[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static <T> void swap(T[] array, int index1, int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void swap(int[] array, int index1, int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void reverse(long[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void swap(long[] array, int index1, int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void reverse(double[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void swap(double[] array, int index1, int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void sort(@NotNull IntComparator comparator, int @NotNull ... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(int fromIndex, int toIndex, @NotNull IntComparator comparator, int @NotNull ... array) {
        checkBounds(array.length, fromIndex, toIndex);
        IntTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(@NotNull LongComparator comparator, long @NotNull ... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(int fromIndex, int toIndex, @NotNull LongComparator comparator, long @NotNull ... array) {
        checkBounds(array.length, fromIndex, toIndex);
        LongTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(@NotNull DoubleComparator comparator, double @NotNull ... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(int fromIndex, int toIndex, @NotNull DoubleComparator comparator, double @NotNull ... array) {
        checkBounds(array.length, fromIndex, toIndex);
        DoubleTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean isSorted(T @NotNull ... array) {
        return Sequence.of(array).zipWithNext().map(Comparable::compareTo).all((comparison -> comparison <= 0));
    }

    @SafeVarargs
    public static <T> boolean isSorted(@NotNull Comparator<T> comparator, T @NotNull ... array) {
        return Sequence.of(array).zipWithNext().map(comparator::compare).all((comparison -> comparison <= 0));
    }

    public static boolean isSorted(int @NotNull ... array) {
        return IntSequence.of(array).zipWithNext(Integer::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(@NotNull IntComparator intComparator, int @NotNull ... array) {
        return IntSequence.of(array).zipWithNext(intComparator::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(long @NotNull ... array) {
        return LongSequence.of(array).zipWithNext(Long::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(@NotNull LongComparator longComparator, long @NotNull ... array) {
        return LongSequence.of(array).zipWithNext(longComparator::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(double @NotNull ... array) {
        return DoubleSequence.of(array).zipWithNext(Double::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(@NotNull DoubleComparator doubleComparator, double @NotNull ... array) {
        return DoubleSequence.of(array).zipWithNext(doubleComparator::compare).all(comparison -> comparison <= 0);
    }

    private static void checkBounds(int length, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("fromIndex < 0");
        }
        if (toIndex > length) {
            throw new ArrayIndexOutOfBoundsException("toIndex > a.length");
        }
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex > toIndex");
        }
    }
}
