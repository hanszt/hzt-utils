package org.hzt.utils.collections;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public interface SequencedSetX<E> extends SetX<E>, SequencedCollectionX<E> {

    static <E> SequencedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> SequencedSetX<E> of(final Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> SequencedSetX<E> of(final Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> SequencedSetX<E> of(final Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> SequencedSetX<E> of(final E first, final E... others) {
        return new LinkedHashSetX<>(first, others);
    }

    static <E> SequencedSetX<E> build(final Consumer<? super MutableSetX<E>> mutableSetConsumer) {
        return new LinkedHashSetX<>(mutableSetConsumer);
    }

    static <E> SequencedSetX<E> build(int size, final Consumer<? super MutableSetX<E>> mutableSetConsumer) {
        return new LinkedHashSetX<>(size, mutableSetConsumer);
    }
}
