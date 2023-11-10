package org.hzt.utils.collections;


import java.util.Collection;
import java.util.NavigableSet;
import java.util.Spliterator;
import java.util.function.Function;

public interface SortedMutableSetX<E> extends NavigableSet<E>, MutableSetX<E> {

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> comparingBy(
            Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(selector);
    }

    static <E> SortedMutableSetX<E> of(NavigableSet<E> set) {
        return new TreeSetX<>(set);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            Iterable<E> iterable,
            Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(iterable, selector);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            Collection<E> collection,
            Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(collection, selector);
    }

    @SafeVarargs
    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(Function<E, R> selector, E first, E... others) {
        return new TreeSetX<>(selector, first, others);
    }

    @Override
    E first();

    @Override
    E last();

    default SortedMutableSetX<E> toNavigableSet() {
        return this;
    }

    @Override
    default Spliterator<E> spliterator() {
        return NavigableSet.super.spliterator();
    }
}
