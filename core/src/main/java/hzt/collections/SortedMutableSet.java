package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.function.Function;
import java.util.stream.Stream;

public interface SortedMutableSet<E> extends NavigableSet<E>, MutableSet<E> {

    static <E, R extends Comparable<R>> SortedMutableSet<E> comparingBy(Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(selector);
    }

    static <E> SortedMutableSet<E> of(NavigableSet<E> set) {
        return new TreeSetX<>(set);
    }

    static <E, R extends Comparable<R>> SortedMutableSet<E> of(Iterable<E> iterable, Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(iterable, selector);
    }

    static <E, R extends Comparable<R>> SortedMutableSet<E> of(Collection<E> collection, Function<? super E, ? extends R> selector) {
        return new TreeSetX<>(collection, selector);
    }

    @SafeVarargs
    static <E, R extends Comparable<R>> SortedMutableSet<E> of(Function<E, R> selector, E first, E... others) {
        return new TreeSetX<>(selector, first, others);
    }

    @Override
    default Stream<E> stream() {
        return MutableSet.super.stream();
    }

    @Override
    @NotNull E first();

    @Override
    @NotNull E last();

    default SortedMutableSet<E> toNavigableSet() {
        return this;
    }
}
