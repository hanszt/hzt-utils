package org.hzt.utils.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

final class ArrayListX<E> extends AbstractList<E> implements MutableListX<E> {

    private final List<E> list;
    private boolean isUnmodifiable = false;

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
        Collections.addAll(list, values);
    }

    ArrayListX(final E value) {
        list = new ArrayList<>(1);
        list.add(value);
    }

    ArrayListX(final Consumer<? super MutableListX<E>> factory) {
        this();
        factory.accept(this);
        isUnmodifiable = true;
    }

    ArrayListX(int size, final Consumer<? super MutableListX<E>> factory) {
        this(size);
        factory.accept(this);
        isUnmodifiable = true;
    }

    @Override
    public ListX<E> shuffled(final Random random) {
        final var listX = new ArrayListX<>(this);
        Collections.shuffle(listX, random);
        return listX;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        throwIfNotModifiable();
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        throwIfNotModifiable();
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throwIfNotModifiable();
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throwIfNotModifiable();
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        throwIfNotModifiable();
        list.clear();
    }

    @Override
    public E get(final int index) {
        return list.get(index);
    }

    @Override
    public E set(final int index, final E element) {
        throwIfNotModifiable();
        return list.set(index, element);
    }

    @Override
    public void add(final int index, final E element) {
        throwIfNotModifiable();
        list.add(index, element);
    }

    @Override
    public E remove(final int index) {
        throwIfNotModifiable();
        return list.remove(index);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        throwIfNotModifiable();
        super.sort(c);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throwIfNotModifiable();
        super.replaceAll(operator);
    }

    @Override
    public int indexOf(final Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(final int index) {
        return list.listIterator(index);
    }

    @Override
    public MutableListX<E> headTo(final int toIndex) {
        return subList(0, toIndex);
    }

    @Override
    public MutableListX<E> tailFrom(final int fromIndex) {
        return subList(fromIndex, size());
    }

    @Override
    public MutableListX<E> subList(final int fromIndex, final int toIndex) {
        final var subList = list.subList(fromIndex, toIndex);
        return isUnmodifiable ?
                new ArrayListX<>(subList.size(), l -> l.addAll(subList)) :
                MutableListX.of(subList);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List) && !(o instanceof ListX)) {
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

    private void throwIfNotModifiable() {
        if (isUnmodifiable) {
            throw new UnsupportedOperationException();
        }
    }
}
