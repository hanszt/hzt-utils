package org.hzt.utils.collections;


import java.util.Collection;
import java.util.NavigableSet;
import java.util.Spliterator;
import java.util.function.Function;

public interface SortedMutableSetX<E> extends NavigableSet<E>, MutableSetX<E> {

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> comparingBy(
            final Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(selector);
    }

    static <E> SortedMutableSetX<E> of(final NavigableSet<E> set) {
        return new TreeSetX<>(set);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            final Iterable<E> iterable,
            final Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(iterable, selector);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            final Collection<E> collection,
            final Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(collection, selector);
    }

    @SafeVarargs
    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(final Function<E, R> selector, final E first, final E... others) {
        return new TreeSetX<>(selector, first, others);
    }

    @Override
    E first();

    @Override
    E last();

    @Override
    default Spliterator<E> spliterator() {
        return NavigableSet.super.spliterator();
    }

    @Override
    default E removeFirst() {
        return NavigableSet.super.removeFirst();
    }

    @Override
    default E removeLast() {
        return NavigableSet.super.removeLast();
    }
}
