package org.hzt.utils.ranges;

import org.jetbrains.annotations.NotNull;

public interface ClosedRange<T extends Comparable<? super T>> {

    @NotNull T start();

    @NotNull T endInclusive();

    default boolean contains(final T value) {
        return 0 <= value.compareTo(start()) && value.compareTo(endInclusive()) <= 0;
    }

    default boolean isEmpty() {
        return start().compareTo(endInclusive()) > 0;
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
