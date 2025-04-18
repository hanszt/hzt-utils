package org.hzt.utils.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.SequencedSet;
import java.util.function.Consumer;

final class LinkedHashSetX<E> extends AbstractSet<E> implements MutableSequencedSetX<E>, SequencedSetX<E> {

    private final SequencedSet<E> set;
    boolean isUnmodifiable = false;

    LinkedHashSetX() {
        this.set = new LinkedHashSet<>();
    }

    LinkedHashSetX(final int n) {
        this.set = LinkedHashSet.newLinkedHashSet(n);
    }

    /**
     * Only for internal use.
     *
     * @param sequencedSet   the source set.
     * @param isUnmodifiable a flag to tell if the set is unmodifiable.
     */
    private LinkedHashSetX(final SequencedSet<E> sequencedSet, boolean isUnmodifiable) {
        this.set = sequencedSet;
        this.isUnmodifiable = isUnmodifiable;
    }

    LinkedHashSetX(final Collection<E> collection) {
        this.set = new LinkedHashSet<>(collection);
    }

    LinkedHashSetX(final Iterable<E> iterable) {
        set = new LinkedHashSet<>();
        for (final var e : iterable) {
            set.add(e);
        }
    }

    @SafeVarargs
    LinkedHashSetX(final E first, final E... others) {
        set = new LinkedHashSet<>();
        set.add(first);
        Collections.addAll(set, others);
    }

    LinkedHashSetX(final Consumer<? super MutableSetX<E>> factory) {
        this();
        factory.accept(this);
        isUnmodifiable = true;
    }

    LinkedHashSetX(int size, final Consumer<? super MutableSetX<E>> factory) {
        this(size);
        factory.accept(this);
        isUnmodifiable = true;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(final Object value) {
        return set.contains(value);
    }

    @Override
    public Iterator<E> iterator() {
        final var iterator = set.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throwIfUnmodifiable();
                iterator.remove();
            }
        };
    }

    @Override
    public boolean add(final E e) {
        throwIfUnmodifiable();
        return set.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        throwIfUnmodifiable();
        return set.remove(o);
    }

    @Override
    public LinkedHashSetX<E> reversed() {
        return new LinkedHashSetX<>(set.reversed(), isUnmodifiable);
    }

    private void throwIfUnmodifiable() {
        if (isUnmodifiable) {
            throw new UnsupportedOperationException();
        }
    }
}
