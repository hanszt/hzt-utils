package org.hzt.utils.tuples;

public record IndexedValue<T>(int index, T value) {

    public static <T> IndexedValue<T> of(int index, T value) {
        return new IndexedValue<>(index, value);
    }
}
