package org.hzt.utils.arrays;

import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Comparator;
import java.util.function.Predicate;

public final class ArraysX {

    private ArraysX() {
    }

    public static <T> boolean[] toBooleanArray(T[] array, Predicate<? super T> predicate) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    public static boolean isSorted(int[] array) {
        return IntSequence.of(array).zipWithNext(Integer::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(long[] array) {
        return LongSequence.of(array).zipWithNext(Long::compare).all(comparison -> comparison <= 0);
    }

    public static boolean isSorted(double[] array) {
        return DoubleSequence.of(array).zipWithNext(Double::compare).all(comparison -> comparison <= 0);
    }

    public static <T> boolean isSorted(T[] array, Comparator<T> comparator) {
        return Sequence.of(array).zipWithNext().all((first, second) -> comparator.compare(first, second) <= 0);
    }

}
