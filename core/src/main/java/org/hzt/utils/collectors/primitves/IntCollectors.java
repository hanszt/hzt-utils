package org.hzt.utils.collectors.primitves;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;

import java.util.function.Function;

public final class IntCollectors {

    private IntCollectors() {
    }

    public static IntCollector<IntMutableList, IntList> toList() {
        return IntCollector.of(IntMutableList::empty, IntMutableList::add, IntMutableList::plus, IntMutableList::toList);
    }

    public static IntCollector<IntMutableList, IntMutableList> toMutableList() {
        return IntCollector.of(IntMutableList::empty, IntMutableList::add, IntMutableList::plus, It::self);
    }

    public static <A, R, R1> IntCollector<A, R1> collectingAndThen(final IntCollector<A, R> downStream, final Function<R, R1> finisher) {
        return new IntCollectorImpl<>(
                downStream.supplier(),
                downStream.accumulator(),
                downStream.combiner(),
                downStream.finisher().andThen(finisher),
                downStream.characteristics());
    }
}
