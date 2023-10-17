package org.hzt.utils.streams;


import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

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

    default <A, V> Collector<T, ?, V> collect(@NotNull Collector<? super R, A, V> collector) {
        return Collector.of(
                () -> new SpinedBuffer<T>(),
                SpinedBuffer::accept,
                (buffer1, buffer2) -> {
                    buffer1.forEach(buffer2);
                    return buffer1;
                },
                buffer -> {
                    final var spliterator = buffer.spliterator();
                    return extend(stream(() -> spliterator, spliterator.characteristics(), false)).collect(collector);
                }
        );
    }
}
