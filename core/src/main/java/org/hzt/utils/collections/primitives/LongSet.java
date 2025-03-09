package org.hzt.utils.collections.primitives;

import org.hzt.utils.Sizable;

import java.util.Collection;
import java.util.function.Consumer;

public interface LongSet extends LongCollection {

    static LongSet of(final long... values) {
        return build(values.length, ms -> {
            for (final var v : values) {
                ms.add(v);
            }
        });
    }

    static LongSet of(final Iterable<Long> iterable) {
        return switch (iterable) {
            case LongHashSet s when s.isUnmodifiable -> s;
            case Collection<Long> c -> build(c.size(), ms -> ms.addAll(iterable));
            case Sizable c -> build(c.size(), ms -> ms.addAll(iterable));
            default -> build(ms -> ms.addAll(iterable));
        };
    }

    static LongSet build(Consumer<LongMutableSet> factory) {
        return new LongHashSet(factory);
    }

    static LongSet build(int size, Consumer<LongMutableSet> factory) {
        return new LongHashSet(size, factory);
    }
}
