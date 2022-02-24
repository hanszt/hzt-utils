package hzt.collectors.primitves;

import hzt.collections.primitives.LongListX;
import hzt.collections.primitives.LongMutableListX;

public final class LongCollectors {

    private LongCollectors() {
    }

    public static LongCollector<LongMutableListX, LongListX> toList() {
        return LongCollector.of(LongMutableListX::empty,
                LongMutableListX::add, LongMutableListX::plus, LongMutableListX::toListX);
    }
}
