package org.hzt.utils.collectors.primitves;

import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.hzt.utils.It;

public final class IntCollectors {

    private IntCollectors() {
    }

    public static IntCollector<IntMutableListX, IntListX> toList() {
        return IntCollector.of(IntMutableListX::empty, IntMutableListX::add, IntMutableListX::plus, IntMutableListX::toListX);
    }

    public static IntCollector<IntMutableListX, IntMutableListX> toMutableList() {
        return IntCollector.of(IntMutableListX::empty, IntMutableListX::add, IntMutableListX::plus, It::self);
    }
}
