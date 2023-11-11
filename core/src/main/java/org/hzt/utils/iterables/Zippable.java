package org.hzt.utils.iterables;

import org.hzt.utils.sequences.EntrySequence;

import java.util.Map;
import java.util.function.BiFunction;

public interface Zippable<T> extends Iterable<T> {

    default <R> EntrySequence<T, R> zip(final Iterable<R> iterable) {
        return EntrySequence.of(zip(iterable, Map::entry));
    }

    <A, R> Zippable<R> zip(Iterable<A> iterable, BiFunction<? super T, ? super A, ? extends R> function);

    default EntrySequence<T, T> zipWithNext() {
        return EntrySequence.of(zipWithNext(Map::entry));
    }

    <R> Zippable<R> zipWithNext(BiFunction<? super T, ? super T, ? extends R> function);
}
