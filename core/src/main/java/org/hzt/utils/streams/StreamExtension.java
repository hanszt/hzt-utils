package org.hzt.utils.streams;


import java.util.Objects;
import java.util.stream.Stream;

@FunctionalInterface
public interface StreamExtension<T, R> {

    Stream<R> extend(Stream<T> sequence);

    default <V> StreamExtension<T, V> andThen(StreamExtension<R, V> after) {
        Objects.requireNonNull(after);
        return tStream -> after.extend(extend(tStream));
    }

    default <V> StreamExtension<V, R> compose(StreamExtension<V, T> before) {
        Objects.requireNonNull(before);
        return vStream -> extend(before.extend(vStream));
    }
}
