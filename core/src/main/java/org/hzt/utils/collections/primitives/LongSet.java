package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface LongSet extends LongCollection {

    static LongSet of(final long... values) {
        return new LongHashSet(values.length, ms -> {
            for (final var v : values) {
                ms.add(v);
            }
        });
    }

    static LongSet of(final Iterable<Long> iterable) {
        return switch (iterable) {
            case Collection<Long> c -> new LongHashSet(c.size(), ms -> fillSet(iterable, ms));
            case CollectionX<Long> c -> new LongHashSet(c.size(), ms -> fillSet(iterable, ms));
            default -> new LongHashSet(ms -> fillSet(iterable, ms));
        };
    }

    private static void fillSet(final Iterable<Long> iterable, final LongMutableSet ms) {
        final var iterator = iterable.iterator();
        if (iterator instanceof PrimitiveIterator.OfLong pi) {
            while (pi.hasNext()) {
                ms.add(pi.nextLong());
            }
            return;
        }
        for (final var l : iterable) {
            ms.add(l);
        }
    }

    static LongSet build(Consumer<LongMutableSet> factory) {
        return new LongHashSet(factory);
    }

    static LongSet build(int size, Consumer<LongMutableSet> factory) {
        return new LongHashSet(size, factory);
    }
}
