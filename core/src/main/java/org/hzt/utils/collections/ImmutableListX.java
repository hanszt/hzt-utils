package org.hzt.utils.collections;

import org.hzt.utils.iterables.IterableXHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

final class ImmutableListX<T> implements ListX<T> {

    private final List<T> immutableList;

    @SafeVarargs
    ImmutableListX(T... values) {
        this.immutableList = List.of(values);
    }

    ImmutableListX(Iterable<T> iterable) {
        immutableList = StreamSupport.stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
    }

    ImmutableListX(Collection<T> collection) {
        immutableList = List.copyOf(collection);
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
        final var listX = to(MutableListX::empty);
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutableListX<?> that = (ImmutableListX<?>) o;
        return Objects.equals(immutableList, that.immutableList);
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
