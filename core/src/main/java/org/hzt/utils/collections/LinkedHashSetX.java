package org.hzt.utils.collections;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

final class LinkedHashSetX<E> extends AbstractSet<E> implements MutableLinkedSetX<E>, LinkedSetX<E> {

    private final Set<E> set;

    LinkedHashSetX() {
        this.set = new LinkedHashSet<>();
    }

    LinkedHashSetX(final int n) {
        this.set = new LinkedHashSet<>(n);
    }

    LinkedHashSetX(final Collection<E> collection) {
        this.set = new LinkedHashSet<>(collection);
    }

    LinkedHashSetX(final Iterable<E> iterable) {
        set = new LinkedHashSet<>();
        for (final E e : iterable) {
            set.add(e);
        }
    }

    @SafeVarargs
    LinkedHashSetX(final E first, final E... others) {
        set = new LinkedHashSet<>();
        set.add(first);
        Collections.addAll(set, others);
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
        return set.iterator();
    }

    @Override
    public boolean add(final E e) {
        return set.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return set.remove(o);
    }
}
