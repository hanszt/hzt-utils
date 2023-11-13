package org.hzt.utils.collections;


import org.hzt.utils.streams.StreamX;

import java.util.Collection;
import java.util.Spliterator;
import java.util.stream.Stream;

public interface MutableCollectionX<E> extends Collection<E>, CollectionX<E> {

    int size();

    @Override
    default StreamX<E> stream() {
        return StreamX.of(Collection.super.stream());
    }

    @Override
    default Stream<E> parallelStream() {
        return Collection.super.parallelStream();
    }

    @Override
    default MutableListX<E> plus(final E value) {
        return (MutableListX<E>) CollectionX.super.plus(value);
    }

    @Override
    default MutableListX<E> plus(final Iterable<? extends E> iterable) {
        return MutableListX.of(this).plus(iterable);
    }

    default boolean addAll(final Iterable<? extends E> iterable) {
        if (iterable instanceof Collection<?>) {
            return addAll((Collection<? extends E>) iterable);
        }
        boolean allAdded = true;
        for (final E item : iterable) {
            if (!add(item)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    default boolean isEmpty()  {
        return CollectionX.super.isEmpty();
    }

    boolean contains(Object value);

    boolean containsAll(Collection<?> c);

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
