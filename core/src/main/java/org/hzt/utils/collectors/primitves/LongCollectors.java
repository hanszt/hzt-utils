package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.collections.primitives.LongMutableListX;

public final class LongCollectors {

    private LongCollectors() {
    }

    public static LongCollector<LongMutableListX, LongListX> toList() {
        return LongCollector.of(LongMutableListX::empty,
                LongMutableListX::add, LongMutableListX::plus, LongMutableListX::toListX);
    }
}
