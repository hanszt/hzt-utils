package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.primitives.DoubleMutableListX;
import org.hzt.utils.ranges.DoubleRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleGroupableTest {

    @Test
    void testGroupBy() {
        final MapX<String, DoubleMutableListX> entries = DoubleRange
                .closed(-10 * Math.PI, 10 * Math.PI, 0.5 * Math.PI)
                .map(x -> x % (2 * Math.PI))
                .groupBy(String::valueOf);

        final int maxSize = entries.maxOf(entry -> entry.getValue().size());

        assertEquals(4, maxSize);
    }
}
