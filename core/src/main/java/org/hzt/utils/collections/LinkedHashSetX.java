package org.hzt.utils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

final class LinkedHashSetX<E> implements MutableLinkedSetX<E>, LinkedSetX<E> {

    private final Set<E> set;

    LinkedHashSetX() {
        this.set = new LinkedHashSet<>();
    }

    LinkedHashSetX(final int n) {
        this.set = LinkedHashSet.newLinkedHashSet(n);
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
    LinkedHashSetX(final E first, final E @NotNull ... others) {
        set = new LinkedHashSet<>();
        set.add(first);
        Collections.addAll(set, others);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var that = (LinkedHashSetX<?>) o;
        return set.equals(that.set);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set);
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

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return set.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull final T @NotNull [] a) {
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
    public boolean containsAll(@NotNull final Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean isNotEmpty() {
        return !isEmpty();
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
