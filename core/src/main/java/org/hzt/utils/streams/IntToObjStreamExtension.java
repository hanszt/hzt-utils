package org.hzt.utils.streams;

import org.hzt.utils.collectors.primitves.IntCollector;
import org.hzt.utils.spined_buffers.SpinedBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.intStream;

@FunctionalInterface
public interface IntToObjStreamExtension<R> {

    Stream<R> extend(IntStream stream);

    default <V> IntToObjStreamExtension<V> andThen(StreamExtension<R, V> after) {
        Objects.requireNonNull(after);
        return tStream -> after.extend(extend(tStream));
    }

    default <A, V> IntCollector<?, V> collect(@NotNull Collector<? super R, A, V> collector) {
        return IntCollector.of(
                SpinedBuffer.OfInt::new,
                SpinedBuffer.OfInt::accept,
                (sb1, sb2) -> {
                    sb2.forEach(sb1);
                    return sb1;
                },
                buffer -> {
                    final var spliterator = buffer.spliterator();
                    return extend(intStream(() -> spliterator, spliterator.characteristics(), false)).collect(collector);
                }
        );
    }
}
