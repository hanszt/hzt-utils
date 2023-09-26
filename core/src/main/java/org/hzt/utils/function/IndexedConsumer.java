package org.hzt.utils.function;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface IndexedConsumer<T> extends Consumer<T> {

    void accept(int index, T value);

    default void accept(final T value) {
        accept(0, value);
    }

    default IndexedConsumer<T> andThen(final IndexedConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (i, t) -> {
            accept(i, t);
            after.accept(i, t);
        };
    }
}
