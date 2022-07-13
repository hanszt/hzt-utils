package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.It;

public final class IntCollectors {

    private IntCollectors() {
    }

    public static IntCollector<IntMutableList, IntList> toList() {
        return IntCollector.of(IntMutableList::empty, IntMutableList::add, IntMutableList::plus, IntMutableList::toList);
    }

    public static IntCollector<IntMutableList, IntMutableList> toMutableList() {
        return IntCollector.of(IntMutableList::empty, IntMutableList::add, IntMutableList::plus, It::self);
    }
}
