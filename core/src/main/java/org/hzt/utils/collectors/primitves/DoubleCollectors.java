package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableCollection;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.Transformable;

import java.util.function.Supplier;

public final class DoubleCollectors {

    private DoubleCollectors() {
    }

    public static DoubleCollector<DoubleMutableList, DoubleList> toList() {
        return DoubleCollector.of(DoubleMutableList::empty,
                DoubleMutableList::add, DoubleMutableList::plus, DoubleMutableList::toList);
    }

    public static DoubleCollector<DoubleMutableList, DoubleMutableList> toMutableList() {
        return to(DoubleMutableList::empty);
    }

    public static <C extends DoubleMutableCollection> DoubleCollector<DoubleMutableList, C>
    to(Supplier<C> doubleCollectionFactory) {
        return DoubleCollector.of(DoubleMutableList::empty,
                DoubleMutableCollection::add, DoubleMutableCollection::plus,
                doubles -> Transformable.from(doubleCollectionFactory.get()).also(c -> c.addAll(doubles)));
    }
}
