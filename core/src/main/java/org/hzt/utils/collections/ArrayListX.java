package org.hzt.utils.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

final class ArrayListX<E> extends AbstractList<E> implements MutableListX<E> {

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
        Collections.addAll(list, values);
    }

    ArrayListX(final E value) {
        list = new ArrayList<>(1);
        list.add(value);
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
        return list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
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
        return MutableListX.of(list.subList(fromIndex, toIndex));
    }
}
