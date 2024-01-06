package org.hzt.utils.iterables;

import org.hzt.utils.sequences.Sequence;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static java.util.Spliterators.iterator;
import static java.util.Spliterators.spliteratorUnknownSize;

public final class IterableExtensions {

    private IterableExtensions() {
    }

    public static <T> IterableExtension<T, List<T>> windowed(final int size,
                                                             final int step) {
        return iterable -> Sequence.of(iterable).windowed(size, step, true).map(Collectable::toList);
    }

    public static <T> IterableExtension<T, List<T>> windowed(final int size) {
        return iterable -> Sequence.of(iterable).windowed(size, 1, false).map(Collectable::toList);
    }

    public static <T> IterableExtension<T, List<T>> windowed(final int size,
                                                             final int step,
                                                             final boolean partialWindows) {
        return iterable -> Sequence.of(iterable).windowed(size, step, partialWindows).map(Collectable::toList);
    }

    public static <T> IterableExtension<T, List<T>> windowSliding(final int size) {
        return s -> () -> iterator(windowSlidingSpliterator(spliteratorUnknownSize(s.iterator(), Spliterator.ORDERED), size));
    }

    public static <T> IterableExtension<T, List<T>> chunked(final int size) {
        return windowed(size, size, true);
    }

    public static <T, R> IterableExtension<T, R> runningFold(final R initial, final BiFunction<R, T, R> function) {
        return iterable -> Sequence.of(iterable).scan(initial, function);
    }

    public static <T> IterableExtension<T, T> take(final long count) {
        return iterable -> Sequence.of(iterable).take(count);
    }

    private static <T> Spliterator<List<T>> windowSlidingSpliterator(final Spliterator<T> spliterator, final int size) {
        final Deque<T> container = new ArrayDeque<>(size);
        return new Spliterators.AbstractSpliterator<List<T>>(spliterator.estimateSize() - size - 1, Spliterator.ORDERED) {

            boolean firstWindow = true;

            @Override
            public boolean tryAdvance(final Consumer<? super List<T>> action) {
                //noinspection StatementWithEmptyBody
                while (spliterator.tryAdvance(container::add) && container.size() < size) ;
                if (container.size() == size || firstWindow) {
                    firstWindow = false;
                    action.accept(new ArrayList<>(container));
                    if (!container.isEmpty()) {
                        container.removeFirst();
                    }
                    return true;
                }
                return false;
            }
        };
    }
}
