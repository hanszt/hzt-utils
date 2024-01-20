package org.hzt.utils.collections;


import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class HashSetX<E> extends AbstractSet<E> implements MutableSetX<E> {

    private final Set<E> set;

    HashSetX() {
        this.set = new HashSet<>();
    }

    HashSetX(final int n) {
        this.set = new HashSet<>(n);
    }

    HashSetX(final Collection<E> collection) {
        this.set = new HashSet<>(collection);
    }

    HashSetX(final Iterable<E> iterable) {
        set = new HashSet<>();
        iterable.forEach(set::add);
    }

    @SafeVarargs
    HashSetX(final E... values) {
        set = new HashSet<>();
        for (final var item : values) {
            if (!set.add(item)) {
                throw new IllegalStateException("Duplicate elements in set. This is not allowed");
            }
        }
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
