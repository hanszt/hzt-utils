package org.hzt.utils.ranges;


public interface ClosedRange<T extends Comparable<? super T>> {

    T start();

    T endInclusive();

    default boolean contains(T value) {
        return 0 <= value.compareTo(start()) && value.compareTo(endInclusive()) <= 0;
    }

    default boolean isEmpty() {
        return start().compareTo(endInclusive()) > 0;
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
