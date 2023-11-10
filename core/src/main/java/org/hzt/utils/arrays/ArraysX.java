package org.hzt.utils.arrays;

import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.DoublePredicate;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public final class ArraysX {

    private ArraysX() {
    }

    public static <T> T[] generateArray(final int size, final IntFunction<T> generator, final IntFunction<T[]> arrayFactory) {
        final T[] array = arrayFactory.apply(size);
        Arrays.setAll(array, generator);
        return array;
    }

    public static int[] generateIntArray(final int size, final IntUnaryOperator generator) {
        final int[] ints = new int[size];
        Arrays.setAll(ints, generator);
        return ints;
    }

    public static long[] generateLongArray(final int size, final IntToLongFunction generator) {
        final long[] longs = new long[size];
        Arrays.setAll(longs, generator);
        return longs;
    }

    public static double[] generateDoubleArray(final int size, final IntToDoubleFunction generator) {
        final double[] doubles = new double[size];
        Arrays.setAll(doubles, generator);
        return doubles;
    }

    public static <T> T[] copyOf(final T[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static int[] copyOf(final int[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static long[] copyOf(final long[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static double[] copyOf(final double[] array) {
        return Arrays.copyOf(array, array.length);
    }

    @SafeVarargs
    public static <T> boolean[] toBooleanArray(final Predicate<? super T> predicate, final T... array) {
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(final IntPredicate predicate, final int... array) {
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(final LongPredicate predicate, final long... array) {
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(final DoublePredicate predicate, final double... array) {
        final boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static <T> void reverse(final T[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void reverse(final int[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static <T> void swap(final T[] array, final int index1, final int index2) {
        final T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void swap(final int[] array, final int index1, final int index2) {
        final int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void reverse(final long[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void swap(final long[] array, final int index1, final int index2) {
        final long temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void reverse(final double[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void swap(final double[] array, final int index1, final int index2) {
        final double temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void sort(final IntComparator comparator, final int... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(final int fromIndex, final int toIndex, final IntComparator comparator, final int... array) {
        checkBounds(array.length, fromIndex, toIndex);
        IntTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(final LongComparator comparator, final long... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(final int fromIndex, final int toIndex, final LongComparator comparator, final long... array) {
        checkBounds(array.length, fromIndex, toIndex);
        LongTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(final DoubleComparator comparator, final double... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(final int fromIndex, final int toIndex, final DoubleComparator comparator, final double... array) {
        checkBounds(array.length, fromIndex, toIndex);
        DoubleTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean isSorted(final T... array) {
        return Sequence.of(array).zipWithNext().map(Comparable::compareTo).all((comparison -> comparison <= 0));
    }

    @SafeVarargs
    public static <T> boolean isSorted(final Comparator<T> comparator, final T... array) {
        return Sequence.of(array).zipWithNext().map(comparator::compare).all((comparison -> comparison <= 0));
    }

    public static boolean isSorted(final int... array) {
        return IntSequence.of(array).zipWithNext(Integer::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(final IntComparator intComparator, final int... array) {
        return IntSequence.of(array).zipWithNext(intComparator::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(final long... array) {
        return LongSequence.of(array).zipWithNext(Long::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(final LongComparator longComparator, final long... array) {
        return LongSequence.of(array).zipWithNext(longComparator::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(final double... array) {
        return DoubleSequence.of(array).zipWithNext(Double::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(final DoubleComparator doubleComparator, final double... array) {
        return DoubleSequence.of(array).zipWithNext(doubleComparator::compare).all(comparison -> comparison <= 0);
    }

    private static void checkBounds(final int length, final int fromIndex, final int toIndex) {
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
