package org.hzt.utils.collections;

import java.util.Collection;
import java.util.List;
import java.util.Spliterator;

public interface MutableListX<E> extends List<E>, ListX<E>, MutableCollectionX<E> {

    static <E> MutableListX<E> empty() {
        return new ArrayListX<>();
    }

    static <E> MutableListX<E> withInitCapacity(final int capacity) {
        return new ArrayListX<>(capacity);
    }

    static <E> MutableListX<E> of(final Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> MutableListX<E> of(final Collection<E> collection) {
        return new ArrayListX<>(collection);
    }

    @SafeVarargs
    static <E> MutableListX<E> of(final E... values) {
        return new ArrayListX<>(values);
    }

    static <E> MutableListX<E> of(final E value) {
        return new ArrayListX<>(value);
    }

    @Override
    default MutableListX<E> plus(final E value) {
        add(value);
        return this;
    }

    @Override
    default MutableListX<E> plus(final Iterable<? extends E> iterable) {
        addAll(iterable);
        return this;
    }

    MutableListX<E> headTo(int toIndex);

    MutableListX<E> tailFrom(int fromIndex);

    MutableListX<E> subList(int fromIndex, int toIndex);

    @Override
    default boolean containsAll(final Iterable<E> iterable) {
        return ListX.super.containsAll(iterable);
    }

    @Override
    boolean contains(Object o);

    @Override
    boolean isEmpty();

    @Override
    default E removeFirst() {
        return List.super.removeFirst();
    }

    @Override
    default E removeLast() {
        return List.super.removeLast();
    }

    @Override
    default MutableListX<E> reversed() {
        return MutableListX.of(List.super.reversed());
    }

    @Override
    default MutableListX<E> get() {
        return this;
    }

    @Override
    default Spliterator<E> spliterator() {
        return List.super.spliterator();
    }

    @Override
    default ListX<E> toListX() {
        return ListX.copyOf(this);
    }
}
