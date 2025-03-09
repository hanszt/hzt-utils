package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;

import java.util.Collection;
import java.util.function.Consumer;

public interface DoubleSet extends DoubleCollection {

    static DoubleSet of(final long... values) {
        return new DoubleHashSet(values.length, ms -> {
            for (final var v : values) {
                ms.add(v);
            }
        });
    }

    static DoubleSet of(final Iterable<Double> iterable) {
        return switch (iterable) {
            case DoubleHashSet s when s.isUnmodifiable -> s;
            case Collection<Double> c -> new DoubleHashSet(c.size(), ms -> ms.addAll(iterable));
            case CollectionX<Double> c -> new DoubleHashSet(c.size(), ms -> ms.addAll(iterable));
            case DoubleCollection c -> new DoubleHashSet(c.size(), ms -> ms.addAll(iterable));
            default -> new DoubleHashSet(ms -> ms.addAll(iterable));
        };
    }

    static DoubleSet build(Consumer<DoubleMutableSet> factory) {
        return new DoubleHashSet(factory);
    }

    static DoubleSet build(int size, Consumer<DoubleMutableSet> factory) {
        return new DoubleHashSet(size, factory);
    }
}
