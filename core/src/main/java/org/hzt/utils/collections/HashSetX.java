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

    HashSetX(int n) {
        this.set = new HashSet<>(n);
    }

    HashSetX(Collection<E> collection) {
        this.set = new HashSet<>(collection);
    }

    HashSetX(Iterable<E> iterable) {
        set = new HashSet<>();
        iterable.forEach(set::add);
    }

    @SafeVarargs
    HashSetX(E... values) {
        set = new HashSet<>();
        for (E item : values) {
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
    public boolean contains(Object value) {
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
    public <T> T[] toArray(T[] a) {
        //noinspection SuspiciousToArrayCall
        return set.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return set.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HashSetX<?> hashSetX = (HashSetX<?>) o;
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
    public boolean containsNot(E e) {
        return !contains(e);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
