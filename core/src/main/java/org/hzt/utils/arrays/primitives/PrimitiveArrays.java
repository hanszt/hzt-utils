package org.hzt.utils.arrays.primitives;

import org.hzt.utils.function.primitives.DoubleToByteFunction;
import org.hzt.utils.function.primitives.DoubleToFloatFunction;
import org.hzt.utils.function.primitives.DoubleToShortFunction;
import org.hzt.utils.function.primitives.IntToByteFunction;
import org.hzt.utils.function.primitives.IntToFloatFunction;
import org.hzt.utils.function.primitives.IntToShortFunction;
import org.hzt.utils.function.primitives.LongToByteFunction;
import org.hzt.utils.function.primitives.LongToFloatFunction;
import org.hzt.utils.function.primitives.LongToShortFunction;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

public final class PrimitiveArrays {

    private PrimitiveArrays() {
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

    public static byte[] toByteArray(@NotNull IntToByteFunction function, int @NotNull ... array) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsByte(array[i]);
        }
        return result;
    }

    public static byte[] toByteArray(@NotNull LongToByteFunction function, long @NotNull ... array) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsByte(array[i]);
        }
        return result;
    }

    public static byte[] toByteArray(@NotNull DoubleToByteFunction function, double @NotNull ... array) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsByte(array[i]);
        }
        return result;
    }

    public static short[] toShortArray(@NotNull IntToShortFunction function, int @NotNull ... array) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsShort(array[i]);
        }
        return result;
    }

    public static short[] toShortArray(@NotNull LongToShortFunction function, long @NotNull ... array) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsShort(array[i]);
        }
        return result;
    }

    public static short[] toShortArray(@NotNull DoubleToShortFunction function, double @NotNull ... array) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsShort(array[i]);
        }
        return result;
    }

    public static float[] toFloatArray(@NotNull IntToFloatFunction function, int @NotNull ... array) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsFloat(array[i]);
        }
        return result;
    }

    public static float[] toFloatArray(@NotNull LongToFloatFunction function, long @NotNull ... array) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsFloat(array[i]);
        }
        return result;
    }

    public static float[] toFloatArray(@NotNull DoubleToFloatFunction function, double @NotNull ... array) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsFloat(array[i]);
        }
        return result;
    }

    public static void reverse(int[] array) {
        for (int lowI = 0, highI = array.length - 1; lowI < array.length / 2; lowI++, highI--) {
            swap(array, lowI, highI);
        }
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

    public static boolean isSorted(int @NotNull ... array) {
        return IntSequence.of(array).zipWithNext(Integer::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(@NotNull IntComparator intComparator, int @NotNull ... array) {
        return IntSequence.of(array).zipWithNext(intComparator::compareInt).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(long @NotNull ... array) {
        return LongSequence.of(array).zipWithNext(Long::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(@NotNull LongComparator longComparator, long @NotNull ... array) {
        return LongSequence.of(array).zipWithNext(longComparator::compareLong).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(double @NotNull ... array) {
        return DoubleSequence.of(array).zipWithNext(Double::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(@NotNull DoubleComparator doubleComparator, double @NotNull ... array) {
        return DoubleSequence.of(array).zipWithNext(doubleComparator::compareDouble).all(comparison -> comparison <= 0);
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
