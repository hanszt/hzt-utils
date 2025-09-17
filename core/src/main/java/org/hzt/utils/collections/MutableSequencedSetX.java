package org.hzt.utils.collections;

import java.util.Collection;
import java.util.SequencedSet;
import java.util.Set;

public interface MutableSequencedSetX<E> extends MutableSetX<E>, SequencedSetX<E>, SequencedSet<E> {

    static <E> MutableSequencedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> MutableSequencedSetX<E> withInitCapacity(final int capacity) {
        return new LinkedHashSetX<>(capacity);
    }

    static <E> MutableSequencedSetX<E> of(final Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableSequencedSetX<E> of(final Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableSequencedSetX<E> of(final Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableSequencedSetX<E> of(final E first, final E... others) {
        return new LinkedHashSetX<>(first, others);
    }

    @Override
    default E removeFirst() {
        return SequencedSet.super.removeFirst();
    }

    @Override
    default E removeLast() {
        return SequencedSet.super.removeLast();
    }

    MutableSequencedSetX<E> reversed();
}
