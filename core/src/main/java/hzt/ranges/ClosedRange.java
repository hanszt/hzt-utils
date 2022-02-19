package hzt.ranges;

public interface ClosedRange<T extends Comparable<? super T>> {

    T start();

    T endInclusive();

    default boolean contains(T value) {
        return value.compareTo(start()) >= 0 && value.compareTo(endInclusive()) <= 0;
    }

    default boolean isEmpty() {
        return start().compareTo(endInclusive()) > 0;
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }
}
