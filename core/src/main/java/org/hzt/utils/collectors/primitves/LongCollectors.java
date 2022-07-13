package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableList;

public final class LongCollectors {

    private LongCollectors() {
    }

    public static LongCollector<LongMutableList, LongList> toList() {
        return LongCollector.of(LongMutableList::empty,
                LongMutableList::add, LongMutableList::plus, LongMutableList::toList);
    }
}
