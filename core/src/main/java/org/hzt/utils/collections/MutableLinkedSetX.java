package org.hzt.utils.collections;

import java.util.Collection;
import java.util.Set;

public interface MutableLinkedSetX<E> extends MutableSetX<E> {

    static <E> MutableLinkedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> MutableLinkedSetX<E> withInitCapacity(int capacity) {
        return new LinkedHashSetX<>(capacity);
    }

    static <E> MutableLinkedSetX<E> of(Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSetX<E> of(Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSetX<E> of(Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableLinkedSetX<E> of(E first, E... others) {
        return new LinkedHashSetX<>(first, others);
    }

}
