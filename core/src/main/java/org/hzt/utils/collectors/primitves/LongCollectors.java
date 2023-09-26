package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableList;

import java.util.function.Function;

public final class LongCollectors {

    private LongCollectors() {
    }

    public static LongCollector<LongMutableList, LongList> toList() {
        return LongCollector.of(LongMutableList::empty,
                LongMutableList::add, LongMutableList::plus, LongMutableList::toList);
    }

    public static <A, R, R1> LongCollector<A, R1> collectingAndThen(final LongCollector<A, R> downStream, final Function<R, R1> finisher) {
        return new LongCollectorImpl<>(
                downStream.supplier(),
                downStream.accumulator(),
                downStream.combiner(),
                downStream.finisher().andThen(finisher),
                downStream.characteristics());
    }
}
