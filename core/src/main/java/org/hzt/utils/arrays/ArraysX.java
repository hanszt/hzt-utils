package org.hzt.utils.arrays;

import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Predicate;

public final class ArraysX {

    private ArraysX() {
    }

    @SafeVarargs
    public static <T> boolean[] toBooleanArray(@NotNull Predicate<? super T> predicate, T @NotNull ... array) {
        boolean[] result = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = predicate.test(array[i]);
        }
        return result;
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> boolean isSorted(T @NotNull ... array) {
        return Sequence.of(array).zipWithNext().map(Comparable::compareTo).all((comparison -> comparison <= 0));
    }

    @SafeVarargs
    public static <T> boolean isSorted(@NotNull Comparator<T> comparator, T @NotNull ... array) {
        return Sequence.of(array).zipWithNext().map(comparator::compare).all((comparison -> comparison <= 0));
    }

}
