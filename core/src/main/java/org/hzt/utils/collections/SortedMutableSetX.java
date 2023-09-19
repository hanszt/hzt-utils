package org.hzt.utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.Spliterator;
import java.util.function.Function;

public interface SortedMutableSetX<E> extends NavigableSet<E>, MutableSetX<E> {

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> comparingBy(
            @NotNull Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(selector);
    }

    static <E> SortedMutableSetX<E> of(@NotNull NavigableSet<E> set) {
        return new TreeSetX<>(set);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            @NotNull Iterable<E> iterable,
            @NotNull Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(iterable, selector);
    }

    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(
            @NotNull Collection<E> collection,
            @NotNull Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(collection, selector);
    }

    @SafeVarargs
    static <E, R extends Comparable<? super R>> SortedMutableSetX<E> of(Function<E, R> selector, E first, E... others) {
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

    @Override
    default E removeFirst() {
        return NavigableSet.super.removeFirst();
    }

    @Override
    default E removeLast() {
        return NavigableSet.super.removeLast();
    }
}
