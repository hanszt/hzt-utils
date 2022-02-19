package hzt.iterables;

import hzt.sequences.EntrySequence;
import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface Zippable<T> extends Iterable<T> {

    default <R> EntrySequence<T, R> zip(@NotNull Iterable<R> iterable) {
        return EntrySequence.ofPairs(zip(iterable, Pair::of));
    }

    <A, R> Zippable<R> zip(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function);

    default EntrySequence<T, T> zipWithNext() {
        return EntrySequence.ofPairs(zipWithNext(Pair::of));
    }

    <R> Zippable<R> zipWithNext(BiFunction<T, T, R> function);
}
