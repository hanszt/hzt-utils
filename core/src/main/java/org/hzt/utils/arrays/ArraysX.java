package org.hzt.utils.arrays;

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


}
