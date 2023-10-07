package org.hzt.utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.Spliterator;
import java.util.function.Function;

public interface SortedMutableSetX<E> extends NavigableSet<E>, MutableSetX<E> {

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> comparingBy(
            @NotNull final Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(selector);
    }

    static <E> SortedMutableSetX<E> of(@NotNull final NavigableSet<E> set) {
        return new TreeSetX<>(set);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            @NotNull final Iterable<E> iterable,
            @NotNull final Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(iterable, selector);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            @NotNull final Collection<E> collection,
            @NotNull final Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(collection, selector);
    }

    @SafeVarargs
    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(final Function<E, R> selector, final E first, final E... others) {
        return new TreeSetX<>(selector, first, others);
    }

    @Override
    @NotNull E first();

    @Override
    @NotNull E last();

    @Override
    default Spliterator<E> spliterator() {
        return NavigableSet.super.spliterator();
    }
}
