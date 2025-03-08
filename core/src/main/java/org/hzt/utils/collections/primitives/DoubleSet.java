package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;

import java.util.Collection;
import java.util.PrimitiveIterator;
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
            case Collection<Double> c -> new DoubleHashSet(c.size(), ms -> fillSet(iterable, ms));
            case CollectionX<Double> c -> new DoubleHashSet(c.size(), ms -> fillSet(iterable, ms));
            default -> new DoubleHashSet(ms -> fillSet(iterable, ms));
        };
    }

    private static void fillSet(final Iterable<Double> iterable, final DoubleMutableSet ms) {
        final var iterator = iterable.iterator();
        if (iterator instanceof PrimitiveIterator.OfDouble pi) {
            while (pi.hasNext()) {
                ms.add(pi.nextDouble());
            }
            return;
        }
        for (final var l : iterable) {
            ms.add(l);
        }
    }

    static DoubleSet build(Consumer<DoubleMutableSet> factory) {
        return new DoubleHashSet(factory);
    }

    static DoubleSet build(int size, Consumer<DoubleMutableSet> factory) {
        return new DoubleHashSet(size, factory);
    }
}
