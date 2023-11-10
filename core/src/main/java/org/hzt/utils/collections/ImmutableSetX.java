package org.hzt.utils.collections;

import org.hzt.utils.collectors.CollectorsX;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

final class ImmutableSetX<T> implements SetX<T> {

    private final Set<T> immutableSet;

    @SafeVarargs
    ImmutableSetX(T... values) {
        this.immutableSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }

    ImmutableSetX(Iterable<T> iterable) {
        immutableSet = StreamSupport.stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(CollectorsX.toUnmodifiableSet());
    }

    ImmutableSetX(Collection<T> collection) {
        immutableSet = Collections.unmodifiableSet(new HashSet<>(collection));
    }

    ImmutableSetX(Set<T> set) {
        immutableSet = Collections.unmodifiableSet(set);
    }

    @Override
    public SetX<T> get() {
        return this;
    }

    @Override
    public int size() {
        return immutableSet.size();
    }

    @Override
    public boolean isEmpty() {
        return immutableSet.isEmpty();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean contains(Object value) {
        return immutableSet.contains(value);
    }

    @Override
    public Iterator<T> iterator() {
        return immutableSet.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImmutableSetX<?> that = (ImmutableSetX<?>) o;
        return Objects.equals(immutableSet, that.immutableSet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(immutableSet);
    }

    @Override
    public String toString() {
        return immutableSet.toString();
    }
}
