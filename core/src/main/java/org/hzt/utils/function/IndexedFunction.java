package org.hzt.utils.function;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface IndexedFunction<T, R> extends Function<T, R> {

    R apply(int index, T value);

    default R apply(final T value) {
        return apply(0, value);
    }

    default <V> IndexedFunction<V, R> compose(final IndexedFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (i, v) -> apply(i, before.apply(i, v));
    }

    default <V> IndexedFunction<T, V> andThen(final IndexedFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (i, t) -> after.apply(i, apply(i, t));
    }
}
