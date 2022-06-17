package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
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
import org.jetbrains.annotations.NotNull;

import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;

@SuppressWarnings("DuplicatedCode")
public final class PrimitiveArrays {

    private PrimitiveArrays() {
    }

    public static boolean[] toBooleanArray(int @NotNull [] array, @NotNull IntPredicate predicate) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(long @NotNull [] array, @NotNull LongPredicate predicate) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean[] toBooleanArray(double @NotNull [] array, @NotNull DoublePredicate predicate) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static byte[] toByteArray(int @NotNull [] array, @NotNull IntToByteFunction function) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsByte(array[i]);
        }
        return result;
    }

    public static byte[] toByteArray(long @NotNull [] array, @NotNull LongToByteFunction function) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsByte(array[i]);
        }
        return result;
    }

    public static byte[] toByteArray(double @NotNull [] array, @NotNull DoubleToByteFunction function) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsByte(array[i]);
        }
        return result;
    }

    public static short[] toShortArray(int @NotNull [] array, @NotNull IntToShortFunction function) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsShort(array[i]);
        }
        return result;
    }

    public static short[] toShortArray(long @NotNull [] array, @NotNull LongToShortFunction function) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsShort(array[i]);
        }
        return result;
    }

    public static short[] toShortArray(double @NotNull [] array, @NotNull DoubleToShortFunction function) {
        short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsShort(array[i]);
        }
        return result;
    }

    public static float[] toFloatArray(int @NotNull [] array, @NotNull IntToFloatFunction function) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsFloat(array[i]);
        }
        return result;
    }

    public static float[] toFloatArray(long @NotNull [] array, @NotNull LongToFloatFunction function) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsFloat(array[i]);
        }
        return result;
    }

    public static float[] toFloatArray(double @NotNull [] array, @NotNull DoubleToFloatFunction function) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = function.applyAsFloat(array[i]);
        }
        return result;
    }

    public static void sort(int @NotNull [] array, @NotNull IntComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(int @NotNull [] array, int fromIndex, int toIndex, @NotNull IntComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        IntTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(long @NotNull [] array, @NotNull LongComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(long @NotNull [] array, int fromIndex, int toIndex, @NotNull LongComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        LongTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(double @NotNull [] array, @NotNull DoubleComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(double @NotNull [] array, int fromIndex, int toIndex, @NotNull DoubleComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        DoubleTimSort.sort(array, fromIndex, toIndex, comparator);
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

    static void binarySort(double[] array, int lo, int hi, int start, DoubleComparator comparator) {
        PreConditions.require(lo <= start && start <= hi);
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            double pivot = array[start];
            int left = lo;
            int right = start;

            PreConditions.require(lo <= start);
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (comparator.compareDouble(pivot, array[mid]) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            PreConditions.require(left == right);

            int n = start - left;
            if (n == 2) {
                array[left + 2] = array[left + 1];
                array[left + 1] = array[left];
            } else if (n == 1) {
                array[left + 1] = array[left];
            } else {
                System.arraycopy(array, left, array, left + 1, n);
            }
            array[left] = pivot;
            ++start;
        }
    }

    static void binarySort(int[] array, int lo, int hi, int start, IntComparator comparator) {
        PreConditions.require(lo <= start && start <= hi);
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            int pivot = array[start];
            int left = lo;
            int right = start;

            PreConditions.require(lo <= start);
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (comparator.compareInt(pivot, array[mid]) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            PreConditions.require(left == right);

            int n = start - left;
            if (n == 2) {
                array[left + 2] = array[left + 1];
                array[left + 1] = array[left];
            } else if (n == 1) {
                array[left + 1] = array[left];
            } else {
                System.arraycopy(array, left, array, left + 1, n);
            }
            array[left] = pivot;
            ++start;
        }
    }

    static void binarySort(long[] array, int lo, int hi, int start, LongComparator comparator) {
        PreConditions.require(lo <= start && start <= hi);
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            long pivot = array[start];
            int left = lo;
            int right = start;

            PreConditions.require(lo <= start);
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (comparator.compareLong(pivot, array[mid]) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            PreConditions.require(left == right);

            int n = start - left;
            if (n == 2) {
                array[left + 2] = array[left + 1];
                array[left + 1] = array[left];
            } else if (n == 1) {
                array[left + 1] = array[left];
            } else {
                System.arraycopy(array, left, array, left + 1, n);
            }
            array[left] = pivot;
            ++start;
        }
    }

    static int countRunAndMakeAscending(int[] array, int lo, int hi, IntComparator comparator) {
        PreConditions.require(lo < hi);
        int runHi = lo + 1;
        if (runHi == hi) {
            return 1;
        }
        if (comparator.compareInt(array[runHi++], array[lo]) >= 0) {
            while (runHi < hi && comparator.compareInt(array[runHi], array[runHi - 1]) >= 0) {
                ++runHi;
            }
        } else {
            while (runHi < hi && comparator.compareInt(array[runHi], array[runHi - 1]) < 0) {
                ++runHi;
            }
            reverseRange(array, lo, runHi);
        }
        return runHi - lo;
    }

    private static void reverseRange(int[] array, int lo, int hi) {
        --hi;
        while (lo < hi) {
            int t = array[lo];
            array[lo++] = array[hi];
            array[hi--] = t;
        }
    }

    static int countRunAndMakeAscending(long[] array, int lo, int hi, LongComparator comparator) {
        PreConditions.require(lo < hi);
        int runHi = lo + 1;
        if (runHi == hi) {
            return 1;
        }
        if (comparator.compareLong(array[runHi++], array[lo]) >= 0) {
            while (runHi < hi && comparator.compareLong(array[runHi], array[runHi - 1]) >= 0) {
                ++runHi;
            }
        } else {
            while (runHi < hi && comparator.compareLong(array[runHi], array[runHi - 1]) < 0) {
                ++runHi;
            }
            reverseRange(array, lo, runHi);
        }
        return runHi - lo;
    }

    private static void reverseRange(long[] array, int lo, int hi) {
        --hi;
        while (lo < hi) {
            long t = array[lo];
            array[lo++] = array[hi];
            array[hi--] = t;
        }
    }

    static int countRunAndMakeAscending(double[] array, int lo, int hi, DoubleComparator comparator) {
        PreConditions.require(lo < hi);
        int runHi = lo + 1;
        if (runHi == hi) {
            return 1;
        }
        if (comparator.compareDouble(array[runHi++], array[lo]) >= 0) {
            while (runHi < hi && comparator.compareDouble(array[runHi], array[runHi - 1]) >= 0) {
                ++runHi;
            }
        } else {
            while (runHi < hi && comparator.compareDouble(array[runHi], array[runHi - 1]) < 0) {
                ++runHi;
            }
            reverseRange(array, lo, runHi);
        }
        return runHi - lo;
    }

    private static void reverseRange(double[] array, int lo, int hi) {
        --hi;
        while (lo < hi) {
            double t = array[lo];
            array[lo++] = array[hi];
            array[hi--] = t;
        }
    }
}
