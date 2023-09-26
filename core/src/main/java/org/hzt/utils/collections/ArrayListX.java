package org.hzt.utils.collections;

import org.hzt.utils.iterables.IterableXHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;

final class ArrayListX<E> implements MutableListX<E> {

    private final List<E> list;

    ArrayListX() {
        this.list = new ArrayList<>();
    }

    ArrayListX(final int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
    }

    ArrayListX(final Collection<E> collection) {
        this.list = new ArrayList<>(collection);
    }

    ArrayListX(final Iterable<E> iterable) {
        list = new ArrayList<>();
        for (final var e : iterable) {
            list.add(e);
        }
    }

    @SafeVarargs
    ArrayListX(final E... values) {
        list = new ArrayList<>(values.length + 1);
        list.addAll(List.of(values));
    }

    ArrayListX(final E value) {
        list = new ArrayList<>(1);
        list.add(value);
    }

    @Override
    public Optional<E> findRandom() {
        return isNotEmpty() ? Optional.of(get(IterableXHelper.RANDOM.nextInt(size()))) : Optional.empty();
    }

    @Override
    public ListX<E> shuffled() {
        final var listX = new ArrayListX<>(this);
        Collections.shuffle(listX);
        return listX;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final Object value) {
        return list.contains(value);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull final T1 @NotNull [] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(final E e) {
        return list.add(e);
    }

    @Override
    @SuppressWarnings("squid:S2250")
    public boolean remove(final Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, @NotNull final Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(final int index) {
        return list.get(index);
    }

    @Override
    public E set(final int index, final E element) {
        return list.set(index, element);
    }

    @Override
    public void add(final int index, final E element) {
        list.add(index, element);
    }

    @Override
    public E remove(final int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(final int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public MutableListX<E> headTo(final int toIndex) {
        return subList(0, toIndex);
    }

    @NotNull
    @Override
    public MutableListX<E> tailFrom(final int fromIndex) {
        return subList(fromIndex, size());
    }

    @NotNull
    @Override
    public MutableListX<E> subList(final int fromIndex, final int toIndex) {
        return MutableListX.of(list.subList(fromIndex, toIndex));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return equalsRange((Iterable<?>) o, size());
    }

    private boolean equalsRange(final Iterable<?> other, final int to) {
        final var es = list.toArray();
        if (to > es.length) {
            throw new ConcurrentModificationException();
        }
        final var oit = other.iterator();
        for (var from = 0; from < to; from++) {
            if (!oit.hasNext() || !Objects.equals(es[from], oit.next())) {
                return false;
            }
        }
        return !oit.hasNext();
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        return list.toString();
    }

}
