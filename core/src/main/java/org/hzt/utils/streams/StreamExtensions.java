package org.hzt.utils.streams;

import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public final class StreamExtensions {

    private StreamExtensions() {
    }

    public static <T, R> StreamExtension<T, R> map(Function<? super T, ? extends R> mapper) {
        return tStream -> {
            final var spliterator = tStream.spliterator();
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<R>(spliterator.estimateSize(), spliterator.characteristics()) {

                private final AtomicReference<T> atomicReference = new AtomicReference<>();
                @Override
                public boolean tryAdvance(Consumer<? super R> action) {
                    final var hasNext = spliterator.tryAdvance(atomicReference::set);
                    action.accept(mapper.apply(atomicReference.get()));
                    return hasNext;
                }
            }, tStream.isParallel());
        };
    }

    public static <T> StreamExtension<T, List<T>> chunked(int size) {
        return windowed(size, size, true);
    }

    public static <T> StreamExtension<T, List<T>> windowed(int size,
                                                           int step,
                                                           boolean partialWindows) {
        return stream -> {
            final var map = Sequence.of(stream::iterator)
                    .windowed(size, step, partialWindows)
                    .map(Collectable::toList);
            return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(map.iterator(), Spliterator.ORDERED),
                    Spliterator.ORDERED, false);
        };
    }

    public static <T, R> StreamExtension<T, R> runningFold(R initial, BiFunction<R, T, R> function) {
        return tStream -> {
            final var scan = Sequence.of(tStream::iterator).scan(initial, function);
            return StreamSupport.stream(() -> Spliterators.spliteratorUnknownSize(scan.iterator(), Spliterator.ORDERED),
                    Spliterator.ORDERED, false);
        };

    }
}
