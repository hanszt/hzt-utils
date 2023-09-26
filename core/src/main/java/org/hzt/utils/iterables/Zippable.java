package org.hzt.utils.iterables;

import org.hzt.utils.sequences.EntrySequence;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiFunction;

public interface Zippable<T> extends Iterable<T> {

    default <R> EntrySequence<T, R> zip(@NotNull final Iterable<R> iterable) {
        return EntrySequence.of(zip(iterable, Map::entry));
    }

    <A, R> Zippable<R> zip(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function);

    default EntrySequence<T, T> zipWithNext() {
        return EntrySequence.of(zipWithNext(Map::entry));
    }

    <R> Zippable<R> zipWithNext(@NotNull BiFunction<? super T, ? super T, ? extends R> function);
}
