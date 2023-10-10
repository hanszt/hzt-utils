package org.hzt.utils.sequences;

import org.hzt.utils.iterables.Collectable;

import java.util.List;
import java.util.function.BiFunction;

public final class SequenceExtensions {

    private SequenceExtensions() {
    }

    public static <T> SequenceExtension<T, List<T>> windowed(int size,
                                                             int step,
                                                             boolean partialWindows) {
        return sequence -> sequence.windowed(size, step, partialWindows).map(Collectable::toList);
    }

    public static <T, R> SequenceExtension<T, R> runningFold(R initial, BiFunction<R, T, R> function) {
        return sequence -> sequence.scan(initial, function);
    }
}
