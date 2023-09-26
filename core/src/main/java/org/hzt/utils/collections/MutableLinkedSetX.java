package org.hzt.utils.collections;

import java.util.Collection;
import java.util.Set;

public interface MutableLinkedSetX<E> extends MutableSetX<E> {

    static <E> MutableLinkedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> MutableLinkedSetX<E> withInitCapacity(final int capacity) {
        return new LinkedHashSetX<>(capacity);
    }

    static <E> MutableLinkedSetX<E> of(final Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSetX<E> of(final Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSetX<E> of(final Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableLinkedSetX<E> of(final E first, final E... others) {
        return new LinkedHashSetX<>(first, others);
    }

}
