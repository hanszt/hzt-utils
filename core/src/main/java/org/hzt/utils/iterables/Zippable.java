package org.hzt.utils.iterables;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.sequences.EntrySequence;

import java.util.function.BiFunction;

public interface Zippable<T> extends Iterable<T> {

    default <R> EntrySequence<T, R> zip(Iterable<R> iterable) {
        return EntrySequence.of(zip(iterable, MapX::entry));
    }

    <A, R> Zippable<R> zip(Iterable<A> iterable, BiFunction<? super T, ? super A, ? extends R> function);

    default EntrySequence<T, T> zipWithNext() {
        return EntrySequence.of(zipWithNext(MapX::entry));
    }

    <R> Zippable<R> zipWithNext(BiFunction<? super T, ? super T, ? extends R> function);
}
