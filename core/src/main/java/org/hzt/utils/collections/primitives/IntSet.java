package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;

import java.util.Collection;
import java.util.function.Consumer;

public interface IntSet extends IntCollection {

    static IntSet of(final int... values) {
        return new IntHashSet(values.length, ms -> {
            for (final var v : values) {
                ms.add(v);
            }
        });
    }

    static IntSet of(final Iterable<Integer> iterable) {
        return switch (iterable) {
            case IntHashSet s when s.isUnmodifiable -> s;
            case Collection<Integer> c -> new IntHashSet(c.size(), ms -> ms.addAll(iterable));
            case CollectionX<Integer> c -> new IntHashSet(c.size(), ms -> ms.addAll(iterable));
            case IntCollection c -> new IntHashSet(c.size(), ms -> ms.addAll(iterable));
            default -> new IntHashSet(ms -> ms.addAll(iterable));
        };
    }

    static IntSet build(Consumer<IntMutableSet> factory) {
        return new IntHashSet(factory);
    }

    static IntSet build(int size, Consumer<IntMutableSet> factory) {
        return new IntHashSet(size, factory);
    }
}
