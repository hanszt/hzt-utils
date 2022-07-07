package org.hzt.utils.comparables;

public interface ComparableX<T> extends Comparable<T> {

    static <T extends Comparable<? super T>> boolean greaterThan(T one, T other) {
        return one.compareTo(other) > 0;
    }

    static <T extends Comparable<? super T>> boolean smallerThan(T one, T other) {
        return one.compareTo(other) < 0;
    }

    static <T extends Comparable<? super T>> boolean greaterOrEqual(T one, T other) {
        return one.compareTo(other) >= 0;
    }

    static <T extends Comparable<? super T>> boolean smallerOrEqual(T one, T other) {
        return one.compareTo(other) <= 0;
    }

    default boolean greaterThan(T other) {
        return compareTo(other) > 0;
    }

    default boolean smallerThan(T other) {
        return compareTo(other) < 0;
    }

    default boolean greaterOrEqual(T other) {
        return compareTo(other) >= 0;
    }

    default boolean smallerOrEqual(T other) {
        return compareTo(other) <= 0;
    }
}
