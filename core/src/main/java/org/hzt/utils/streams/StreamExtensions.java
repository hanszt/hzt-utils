package org.hzt.utils.streams;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.List;
import java.util.Objects;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Spliterator.ORDERED;
import static java.util.stream.StreamSupport.stream;

public final class StreamExtensions {

    private StreamExtensions() {
    }

    public static <T, R> StreamExtension<T, R> map(final Function<? super T, ? extends R> mapper) {
        return tStream -> {
            final var spliterator = tStream.spliterator();
            return stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics()) {

                private final AtomicReference<T> atomicReference = new AtomicReference<>();

                @Override
                public boolean tryAdvance(final Consumer<? super R> action) {
                    final var hasNext = spliterator.tryAdvance(atomicReference::set);
                    action.accept(mapper.apply(atomicReference.get()));
                    return hasNext;
                }
            }, tStream.isParallel());
        };
    }

    public static <T> StreamExtension<T, T> peek(final Consumer<? super T> consumer) {
        return map(t -> {
                    consumer.accept(t);
                    return t;
                }
        );
    }

    public static <T> StreamExtension<T, List<T>> chunked(final int size) {
        return windowed(size, size, true);
    }

    public static <T> StreamExtension<T, List<T>> windowed(final int size) {
        return windowed(size, 1);
    }

    public static <T, R> StreamExtension<T, R> windowed(final int size, final Function<List<T>, R> mapper) {
        return windowed(size, 1, false, mapper);
    }

    public static <T> StreamExtension<T, List<T>> windowed(final int size, final int step) {
        return windowed(size, step, false);
    }

    public static <T> StreamExtension<T, List<T>> windowed(final int size,
                                                           final int step,
                                                           final boolean partialWindows) {
        return windowed(size, step, partialWindows, s -> s);
    }

    public static <T, R> StreamExtension<T, R> windowed(final int size,
                                                        final int step,
                                                        final boolean partialWindows,
                                                        final Function<List<T>, R> mapper) {
        return stream -> stream(() -> Spliterators.spliteratorUnknownSize(Sequence.of(stream::iterator)
                .windowed(size, step, partialWindows)
                .map(s -> mapper.apply(s.toList())).iterator(), ORDERED), ORDERED, false);
    }

    public static <T, R> StreamExtension<T, R> zipWithNext(final BiFunction<? super T, ? super T, ? extends R> mapper) {
        return stream -> stream(() -> Spliterators.spliteratorUnknownSize(Sequence.of(stream::iterator)
                .zipWithNext(mapper)
                .iterator(), ORDERED), ORDERED, false);
    }

    public static <T, R> StreamExtension<T, R> scan(final R initial, final BiFunction<R, T, R> function) {
        return tStream -> stream(() -> Spliterators
                .spliteratorUnknownSize(Sequence.of(tStream::iterator)
                        .scan(initial, function).iterator(), ORDERED), ORDERED, false);
    }

    public static <T> StreamExtension<T, T> filter(final Predicate<? super T> predicate) {
        return s -> s.filter(predicate);
    }

    public static <T, R> StreamExtension<T, R> mapNotNull(final Function<? super T, R> mapper) {
        return s -> s.filter(Objects::nonNull).map(mapper).filter(Objects::nonNull);
    }
}
