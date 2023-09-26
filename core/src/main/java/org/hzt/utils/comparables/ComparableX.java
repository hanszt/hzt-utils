package org.hzt.utils.comparables;

public interface ComparableX<T> extends Comparable<T> {

    static <T extends Comparable<? super T>> boolean greaterThan(final T one, final T other) {
        return one.compareTo(other) > 0;
    }

    static <T extends Comparable<? super T>> boolean smallerThan(final T one, final T other) {
        return one.compareTo(other) < 0;
    }

    static <T extends Comparable<? super T>> boolean greaterOrEqual(final T one, final T other) {
        return one.compareTo(other) >= 0;
    }

    static <T extends Comparable<? super T>> boolean smallerOrEqual(final T one, final T other) {
        return one.compareTo(other) <= 0;
    }

    default boolean greaterThan(final T other) {
        return compareTo(other) > 0;
    }

    default boolean smallerThan(final T other) {
        return compareTo(other) < 0;
    }

    default boolean greaterOrEqual(final T other) {
        return compareTo(other) >= 0;
    }

    default boolean smallerOrEqual(final T other) {
        return compareTo(other) <= 0;
    }
}
