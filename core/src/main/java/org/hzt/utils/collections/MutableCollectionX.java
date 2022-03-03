package org.hzt.utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Spliterator;
import java.util.stream.Stream;

public interface MutableCollectionX<E> extends Collection<E>, CollectionX<E> {

    int size();

    @Override
    default Stream<E> stream() {
        return Collection.super.stream();
    }

    @Override
    default Stream<E> parallelStream() {
        return Collection.super.stream();
    }

    @Override
    default MutableListX<E> plus(@NotNull E value) {
        return (MutableListX<E>) CollectionX.super.plus(value);
    }

    @Override
    default MutableListX<E> plus(@NotNull Iterable<E> iterable) {
        return (MutableListX<E>) CollectionX.super.plus(iterable);
    }

    default boolean isEmpty()  {
        return CollectionX.super.isEmpty();
    }

    boolean contains(Object value);

    boolean containsAll(@NotNull Collection<?> c);

    default boolean removeFirst() {
        return remove(first());
    }

    default boolean removeLast() {
        return remove(last());
    }

    @Override
    default Spliterator<E> spliterator() {
        return Collection.super.spliterator();
    }
}
