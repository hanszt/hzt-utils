package org.hzt.utils.iterables;

import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collector;

@FunctionalInterface
public interface IterableExtension<T, R> {

    Iterable<R> extend(Iterable<T> iterable);

    default <V> IterableExtension<T, V> andThen(@NotNull IterableExtension<R, V> after) {
        Objects.requireNonNull(after);
        return iterable -> after.extend(extend(iterable));
    }

    default <V> IterableExtension<V, R> compose(@NotNull IterableExtension<V, T> before) {
        Objects.requireNonNull(before);
        return vIterable -> extend(before.extend(vIterable));
    }

    default <A, V> Collector<T, ?, V> collect(@NotNull Collector<? super R, A, V> collector) {
        return Collector.of(
                SpinedBuffer<T>::new,
                SpinedBuffer::accept,
                Iterables::combine,
                buffer -> Sequence.of(extend(buffer)).collect(collector)
        );
    }
}
