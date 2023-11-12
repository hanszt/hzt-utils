package org.hzt.utils.streams;

import org.hzt.utils.sequences.Sequence;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StreamFinishers {

    private StreamFinishers() {
    }

    public static <T, R> Function<Stream<T>, R> fold(R initial, BiFunction<? super R, ? super T, ? extends R> mapper) {
        return s -> Sequence.of(s::iterator).fold(initial, mapper);
    }

    public static <T> Function<Stream<T>, Set<T>> toSet() {
        return s -> s.collect(Collectors.toUnmodifiableSet());
    }
}
