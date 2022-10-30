package org.hzt.utils.collections;

import org.hzt.utils.iterables.IterableXHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

    ArrayListX(int initialCapacity) {
        this.list = new ArrayList<>(initialCapacity);
    }

    ArrayListX(Collection<E> collection) {
        this.list = new ArrayList<>(collection);
    }

    ArrayListX(Iterable<E> iterable) {
        list = new ArrayList<>();
        for (E e : iterable) {
            list.add(e);
        }
    }

    @SafeVarargs
    ArrayListX(E... values) {
        list = new ArrayList<>(values.length + 1);
        list.addAll(Arrays.asList(values));
    }

    ArrayListX(E value) {
        list = new ArrayList<>(1);
        list.add(value);
    }

    @Override
    public Optional<E> findRandom() {
        return isNotEmpty() ? Optional.of(get(IterableXHelper.RANDOM.nextInt(size()))) : Optional.empty();
    }

    @Override
    public ListX<E> shuffled() {
        final MutableListX<E> listX = to(MutableListX::empty);
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
    public boolean contains(Object value) {
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
    public <T1> T1 @NotNull [] toArray(@NotNull T1 @NotNull [] a) {
        //noinspection SuspiciousToArrayCall
        return list.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return list.add(e);
    }

    @Override
    @SuppressWarnings("squid:S2250")
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @NotNull
    @Override
    public MutableListX<E> headTo(int toIndex) {
        return subList(0, toIndex);
    }

    @NotNull
    @Override
    public MutableListX<E> tailFrom(int fromIndex) {
        return subList(fromIndex, size());
    }

    @NotNull
    @Override
    public MutableListX<E> subList(int fromIndex, int toIndex) {
        return MutableListX.of(list.subList(fromIndex, toIndex));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArrayListX<?> that = (ArrayListX<?>) o;
        return Objects.equals(list, that.list);
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
