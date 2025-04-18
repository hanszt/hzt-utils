package org.hzt.utils.collections.primitives;

import org.hzt.utils.Sizable;
import org.hzt.utils.collections.SetX;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public interface DoubleSet extends DoubleCollection {

    static DoubleSet of(final double... values) {
        return build(values.length, ms -> {
            for (final var v : values) {
                ms.add(v);
            }
        });
    }

    static DoubleSet of(final Iterable<Double> iterable) {
        return switch (iterable) {
            case DoubleHashSet s when s.isUnmodifiable -> s;
            case Collection<Double> c -> build(c.size(), ms -> ms.addAll(iterable));
            case Sizable c -> build(c.size(), ms -> ms.addAll(iterable));
            default -> build(ms -> ms.addAll(iterable));
        };
    }

    static DoubleSet build(Consumer<DoubleMutableSet> factory) {
        return new DoubleHashSet(factory);
    }

    static DoubleSet build(int size, Consumer<DoubleMutableSet> factory) {
        return new DoubleHashSet(size, factory);
    }

    @Override
    default SetX<Double> boxed() {
        return new SetX<>() {
            @Override
            public int size() {
                return DoubleSet.this.size();
            }

            @Override
            public Iterator<Double> iterator() {
                return DoubleSet.this.iterator();
            }

            @Override
            public boolean contains(final Double value) {
                return DoubleSet.this.contains(value);
            }
        };
    }
}
