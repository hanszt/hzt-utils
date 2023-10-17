package org.hzt.utils.iterables;

import org.hzt.utils.sequences.Sequence;

import java.util.List;
import java.util.function.BiFunction;

public final class IterableExtensions {

    private IterableExtensions() {
    }

    public static <T> IterableExtension<T, List<T>> windowed(int size,
                                                             int step,
                                                             boolean partialWindows) {
        return iterable -> Sequence.of(iterable).windowed(size, step, partialWindows).map(Collectable::toList);
    }

    public static <T> IterableExtension<T, List<T>> chunked(int size) {
        return windowed(size, size, true);
    }

    public static <T, R> IterableExtension<T, R> runningFold(R initial, BiFunction<R, T, R> function) {
        return iterable -> Sequence.of(iterable).scan(initial, function);
    }

    public static <T> IterableExtension<T, T> take(long count) {
        return iterable -> Sequence.of(iterable).take(count);
    }
}
