package org.hzt.utils.streams;

import org.hzt.utils.iterables.Iterables;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

@FunctionalInterface
public interface StreamExtension<T, R> {

    Stream<R> extend(Stream<T> stream);

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
                SpinedBuffer<T>::new,
                SpinedBuffer::accept,
                Iterables::combine,
                buffer -> {
                    final var spliterator = buffer.spliterator();
                    return extend(stream(() -> spliterator, spliterator.characteristics(), false)).collect(collector);
                }
        );
    }

    default <V> Function<Stream<T>, V> finish(Function<Stream<R>, V> finisher) {
        return s -> finisher.apply(extend(s));
    }
}
