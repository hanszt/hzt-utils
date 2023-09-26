package org.hzt.utils.collections;

import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.iterables.IterableXHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

final class ImmutableListX<T> implements ListX<T> {

    private final List<T> immutableList;

    @SafeVarargs
    ImmutableListX(T... values) {
        this.immutableList = Collections.unmodifiableList(Arrays.asList(values));
    }

    ImmutableListX(Iterable<T> iterable) {
        immutableList = StreamSupport.stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(CollectorsX.toUnmodifiableList());
    }

    ImmutableListX(Collection<T> collection) {
        immutableList = Collections.unmodifiableList(new ArrayList<>(collection));
    }

    ImmutableListX(List<T> list) {
        immutableList = Collections.unmodifiableList(list);
    }

    @Override
    public @NotNull ListX<T> get() {
        return this;
    }

    @Override
    public Optional<T> findRandom() {
        return isNotEmpty() ? Optional.of(get(IterableXHelper.RANDOM.nextInt(size()))) : Optional.empty();
    }

    @Override
    public ListX<T> shuffled() {
        final MutableListX<T> listX = to(MutableListX::empty);
        Collections.shuffle(listX);
        return ListX.copyOf(listX);
    }

    @Override
    public int size() {
        return immutableList.size();
    }

    @Override
    public boolean isEmpty() {
        return immutableList.isEmpty();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean contains(Object value) {
        return immutableList.contains(value);
    }

    @Override
    public T get(int index) {
        return immutableList.get(index);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int indexOf(Object o) {
        return immutableList.indexOf(o);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int lastIndexOf(Object o) {
        return immutableList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return immutableList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return immutableList.listIterator(index);
    }

    @Override
    public ListX<T> headTo(int toIndex) {
        return subList(0, toIndex);
    }

    @Override
    public ListX<T> tailFrom(int fromIndex) {
        return subList(fromIndex, size());
    }

    @Override
    public ListX<T> subList(int fromIndex, int toIndex) {
        return ListX.copyOf(immutableList.subList(fromIndex, toIndex));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return immutableList.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ListX)) {
            return false;
        }
        return equalsRange((ListX<?>) o, size());
    }

    private boolean equalsRange(Iterable<?> other, int to) {
        final Object[] es = immutableList.toArray();
        if (to > es.length) {
            throw new ConcurrentModificationException();
        }
        Iterator<?> oit = other.iterator();
        for (int from = 0; from < to; from++) {
            if (!oit.hasNext() || !Objects.equals(es[from], oit.next())) {
                return false;
            }
        }
        return !oit.hasNext();
    }

    @Override
    public int hashCode() {
        return Objects.hash(immutableList);
    }

    @Override
    public String toString() {
        return immutableList.toString();
    }
}
