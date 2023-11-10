package org.hzt.utils.collections;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

final class HashSetX<E> implements MutableSetX<E> {

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
        for (final E item : values) {
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
    public boolean isEmpty() {
        return set.isEmpty();
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
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        //noinspection SuspiciousToArrayCall
        return set.toArray(a);
    }

    @Override
    public boolean add(final E e) {
        return set.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HashSetX<?> hashSetX = (HashSetX<?>) o;
        return set.equals(hashSetX.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set);
    }

    @Override
    public boolean isNotEmpty() {
        return !set.isEmpty();
    }

    @Override
    public boolean containsNot(final E e) {
        return !contains(e);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
