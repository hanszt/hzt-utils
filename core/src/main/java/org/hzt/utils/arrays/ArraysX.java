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

    public static <T> T[] generateArray(int size, IntFunction<T> generator, IntFunction<T[]> arrayFactory) {
        final T[] array = arrayFactory.apply(size);
        Arrays.setAll(array, generator);
        return array;
    }

    public static int[] generateIntArray(int size, IntUnaryOperator generator) {
        final int[] ints = new int[size];
        Arrays.setAll(ints, generator);
        return ints;
    }

    public static long[] generateLongArray(int size, IntToLongFunction generator) {
        final long[] longs = new long[size];
        Arrays.setAll(longs, generator);
        return longs;
    }

    public static double[] generateDoubleArray(int size, IntToDoubleFunction generator) {
        final double[] doubles = new double[size];
        Arrays.setAll(doubles, generator);
        return doubles;
    }

    public static <T> T[] copyOf(T[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static int[] copyOf(int[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static long[] copyOf(long[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static double[] copyOf(double[] array) {
        return Arrays.copyOf(array, array.length);
    }

    @SafeVarargs
    public static <T> boolean[] toBooleanArray(Predicate<? super T> predicate, T... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(IntPredicate predicate, int... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(LongPredicate predicate, long... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(DoublePredicate predicate, double... array) {
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
        final T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void swap(int[] array, int index1, int index2) {
        final int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void reverse(long[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void swap(long[] array, int index1, int index2) {
        final long temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void reverse(double[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
    }

    public static void swap(double[] array, int index1, int index2) {
        final double temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void sort(IntComparator comparator, int... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(int fromIndex, int toIndex, IntComparator comparator, int... array) {
        checkBounds(array.length, fromIndex, toIndex);
        IntTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(LongComparator comparator, long... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(int fromIndex, int toIndex, LongComparator comparator, long... array) {
        checkBounds(array.length, fromIndex, toIndex);
        LongTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(DoubleComparator comparator, double... array) {
        sort(0, array.length, comparator, array);
    }

    public static void sort(int fromIndex, int toIndex, DoubleComparator comparator, double... array) {
        checkBounds(array.length, fromIndex, toIndex);
        DoubleTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean isSorted(T... array) {
        return Sequence.of(array).zipWithNext().map(Comparable::compareTo).all((comparison -> comparison <= 0));
    }

    @SafeVarargs
    public static <T> boolean isSorted(Comparator<T> comparator, T... array) {
        return Sequence.of(array).zipWithNext().map(comparator::compare).all((comparison -> comparison <= 0));
    }

    public static boolean isSorted(int... array) {
        return IntSequence.of(array).zipWithNext(Integer::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(IntComparator intComparator, int... array) {
        return IntSequence.of(array).zipWithNext(intComparator::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(long... array) {
        return LongSequence.of(array).zipWithNext(Long::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(LongComparator longComparator, long... array) {
        return LongSequence.of(array).zipWithNext(longComparator::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(double... array) {
        return DoubleSequence.of(array).zipWithNext(Double::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(DoubleComparator doubleComparator, double... array) {
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
