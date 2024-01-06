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
    ImmutableSetX(final T... values) {
        this.immutableSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }

    ImmutableSetX(final Iterable<T> iterable) {
        immutableSet = StreamSupport.stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(CollectorsX.toUnmodifiableSet());
    }

    ImmutableSetX(final Collection<T> collection) {
        immutableSet = Collections.unmodifiableSet(new HashSet<>(collection));
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
    public boolean contains(final Object value) {
        return immutableSet.contains(value);
    }

    @Override
    public Iterator<T> iterator() {
        return immutableSet.iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetX)) {
            return false;
        }

        final CollectionX<?> c = (CollectionX<?>) o;
        if (c.size() != size()) {
            return false;
        }
        for (final Object e : c) {
            if (e == null || !contains(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return immutableSet.hashCode();
    }

    @Override
    public String toString() {
        return immutableSet.toString();
    }
}
