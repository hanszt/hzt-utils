package org.hzt.utils.collections;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

final class UnmodifiableListX<T> implements ListX<T>, RandomAccess {

    private final List<T> unmodifiableList;

    @SafeVarargs
    UnmodifiableListX(final T... values) {
        for (final var item : values) {
            if (item == null) {
                throw new IllegalStateException("No null values allowed!");
            }
        }
        this.unmodifiableList = List.of(values);
    }

    UnmodifiableListX(final Iterable<T> iterable, boolean nullsAllowed) {
        unmodifiableList = switch (iterable) {
            case UnmodifiableListX<T> l -> l.unmodifiableList;
            case ArrayListX<T> l when l.isUnmodifiable -> l;
            case Collection<T> c -> nullsAllowed ? listCopy(iterable, e -> true) : List.copyOf(c);
            default -> nullsAllowed ? listCopy(iterable, e -> true) : listCopy(iterable, Objects::nonNull);
        };
    }

    private static <T> List<T> listCopy(final Iterable<T> iterable, final Predicate<T> predicate) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(predicate)
                .toList();
    }

    @Override
    public ListX<T> get() {
        return this;
    }

    @Override
    public int size() {
        return unmodifiableList.size();
    }

    @Override
    public boolean isEmpty() {
        return unmodifiableList.isEmpty();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean contains(final Object value) {
        return unmodifiableList.contains(value);
    }

    @Override
    public T get(final int index) {
        return unmodifiableList.get(index);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int indexOf(final Object o) {
        return unmodifiableList.indexOf(o);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int lastIndexOf(final Object o) {
        return unmodifiableList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return unmodifiableList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return unmodifiableList.listIterator(index);
    }

    @Override
    public ListX<T> headTo(final int toIndex) {
        return subList(0, toIndex);
    }

    @Override
    public ListX<T> tailFrom(final int fromIndex) {
        return subList(fromIndex, size());
    }

    @Override
    public ListX<T> subList(final int fromIndex, final int toIndex) {
        return ListX.of(unmodifiableList.subList(fromIndex, toIndex));
    }

    @Override
    public Iterator<T> iterator() {
        return unmodifiableList.iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ListX)) {
            return false;
        }
        return equalsRange((ListX<?>) o, size());
    }

    private boolean equalsRange(final Iterable<?> other, final int to) {
        final var es = unmodifiableList.toArray();
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
        return Objects.hash(unmodifiableList);
    }

    @Override
    public String toString() {
        return unmodifiableList.toString();
    }
}
