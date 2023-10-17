package org.hzt.utils.iterables;

import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collector;

@FunctionalInterface
public interface IterableExtension<T, R> {

    Iterable<R> extend(Iterable<T> sequence);

    default <V> IterableExtension<T, V> andThen(@NotNull IterableExtension<R, V> after) {
        Objects.requireNonNull(after);
        return tSequence -> after.extend(extend(tSequence));
    }

    default <V> IterableExtension<V, R> compose(@NotNull IterableExtension<V, T> before) {
        Objects.requireNonNull(before);
        return vSequence -> extend(before.extend(vSequence));
    }

    default <A, V> Collector<T, ?, V> collect(@NotNull Collector<? super R, A, V> collector) {
        return Collector.of(
                () -> new SpinedBuffer<T>(),
                SpinedBuffer::accept,
                (buffer1, buffer2) -> {
                    buffer1.forEach(buffer2);
                    return buffer1;
                },
                buffer -> Sequence.of(extend(buffer)).collect(collector)
        );
    }
}
