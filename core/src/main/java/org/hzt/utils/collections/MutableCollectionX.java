package org.hzt.utils.collections;

import org.hzt.utils.iterables.MutableIterable;

import org.hzt.utils.streams.StreamX;

import java.util.Collection;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface MutableCollectionX<E> extends Collection<E>, CollectionX<E>, MutableIterable<E> {

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
        if (iterable instanceof final Collection<?> c) {
            return addAll((Collection<? extends E>) c);
        }
        var allAdded = true;
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

    @Override
    default boolean removeIf(final Predicate<? super E> filter) {
        return Collection.super.removeIf(filter);
    }

    default E removeFirst() {
        final var first = first();
        remove(first);
        return first;
    }

    default E removeLast() {
        final var last = last();
        remove(last);
        return last;
    }

    @Override
    default Spliterator<E> spliterator() {
        return Collection.super.spliterator();
    }
}
