package hzt.iterables;

import hzt.sequences.EntrySequence;
import hzt.tuples.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface Zippable<T> extends Iterable<T> {

    default <A, R, C extends Collection<R>> C zipTo(
            @NotNull Supplier<C> collectionFactory,
            @NotNull Iterable<A> otherIterable,
            @NotNull BiFunction<? super T, ? super A, ? extends R> function) {
        final Iterator<A> otherIterator = otherIterable.iterator();
        final Iterator<T> iterator = iterator();
        final C list = collectionFactory.get();
        while (iterator.hasNext() && otherIterator.hasNext()) {
            final T next = iterator.next();
            final A otherNext = otherIterator.next();
            list.add(function.apply(next, otherNext));
        }
        return list;
    }

    default <R> EntrySequence<T, R> zip(@NotNull Iterable<R> iterable) {
        return EntrySequence.ofPairs(zip(iterable, Pair::of));
    }

    <A, R> Zippable<R> zip(@NotNull Iterable<A> iterable, @NotNull BiFunction<? super T, ? super A, ? extends R> function);

    default <R, C extends Collection<R>> C zipWithNextTo(@NotNull Supplier<C> collectionFactory,
                                                         @NotNull BiFunction<T, T, R> function) {
        final Iterator<T> iterator = iterator();
        if (!iterator.hasNext()) {
            return collectionFactory.get();
        }
        final C list = collectionFactory.get();
        T current = iterator.next();
        while (iterator.hasNext()) {
            final T next = iterator.next();
            list.add(function.apply(current, next));
            current = next;
        }
        return list;
    }

    default EntrySequence<T, T> zipWithNext() {
        return EntrySequence.ofPairs(zipWithNext(Pair::of));
    }

    <R> Zippable<R> zipWithNext(BiFunction<T, T, R> function);
}
