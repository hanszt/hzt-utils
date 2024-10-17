package org.hzt.utils.ranges;


public interface ClosedRange<T extends Comparable<? super T>> {

    T start();

    T endInclusive();

    default boolean contains(final T value) {
        return 0 <= value.compareTo(start()) && value.compareTo(endInclusive()) <= 0;
    }

    default boolean overlaps(final ClosedRange<T> other) {
        return contains(other.start()) || contains(other.endInclusive()) || other.contains(start()) || other.contains(endInclusive());
    }

    default boolean isEmpty() {
        return start().compareTo(endInclusive()) > 0;
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
