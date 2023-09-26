package org.hzt.utils.collections;

import java.util.Collection;
import java.util.Set;

public interface LinkedSetX<E> extends SetX<E> {

    static <E> LinkedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> LinkedSetX<E> of(final Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> LinkedSetX<E> of(final Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> LinkedSetX<E> of(final Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> LinkedSetX<E> of(final E first, final E... others) {
        return new LinkedHashSetX<>(first, others);
    }
}
