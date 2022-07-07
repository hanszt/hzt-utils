package org.hzt.utils.function.primitives;

public interface IndexedConsumer<T> {

    void accept(int index, T value);
}
