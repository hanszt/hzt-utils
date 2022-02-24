package hzt.collectors.primitves;

import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.IntMutableListX;
import hzt.utils.It;

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
