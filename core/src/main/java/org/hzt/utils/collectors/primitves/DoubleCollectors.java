package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.DoubleListX;
import org.hzt.utils.collections.primitives.DoubleMutableCollection;
import org.hzt.utils.collections.primitives.DoubleMutableListX;
import org.hzt.utils.Transformable;

import java.util.function.Supplier;

public final class DoubleCollectors {

    private DoubleCollectors() {
    }

    public static DoubleCollector<DoubleMutableListX, DoubleListX> toList() {
        return DoubleCollector.of(DoubleMutableListX::empty,
                DoubleMutableListX::add, DoubleMutableListX::plus, DoubleMutableListX::toListX);
    }

    public static DoubleCollector<DoubleMutableListX, DoubleMutableListX> toMutableList() {
        return to(DoubleMutableListX::empty);
    }

    public static <C extends DoubleMutableCollection> DoubleCollector<DoubleMutableListX, C>
    to(Supplier<C> doubleCollectionFactory) {
        return DoubleCollector.of(DoubleMutableListX::empty,
                DoubleMutableCollection::add, DoubleMutableCollection::plus,
                doubles -> Transformable.from(doubleCollectionFactory.get()).also(c -> c.addAll(doubles)));
    }
}
