package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;

import java.util.Collection;
import java.util.PrimitiveIterator;
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
            case Collection<Integer> c -> new IntHashSet(c.size(), ms -> fillSet(iterable, ms));
            case CollectionX<Integer> c -> new IntHashSet(c.size(), ms -> fillSet(iterable, ms));
            default -> new IntHashSet(ms -> fillSet(iterable, ms));
        };
    }

    private static void fillSet(final Iterable<Integer> iterable, final IntMutableSet ms) {
        final var iterator = iterable.iterator();
        if (iterator instanceof PrimitiveIterator.OfInt pi) {
            while (pi.hasNext()) {
                ms.add(pi.nextInt());
            }
            return;
        }
        for (final var l : iterable) {
            ms.add(l);
        }
    }

    static IntSet build(Consumer<IntMutableSet> factory) {
        return new IntHashSet(factory);
    }

    static IntSet build(int size, Consumer<IntMutableSet> factory) {
        return new IntHashSet(size, factory);
    }
}
