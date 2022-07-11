package org.hzt.utils.collections.primitives;

import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.LongRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongMutableSetXTest {

    @Test
    void testLongSetSize() {
        LongMutableSetX set = LongMutableSetX.empty();
        set.addAll(LongListX.of(-1, -2231, 0, 3, 3, 4, 5, 6, 5));

        assertEquals(7, set.size());
    }

    @Test
    void testCountElementsInLongSetThroughSequence() {
        LongMutableSetX set = LongMutableSetX.of(1, 2, 3, 3, 4, 5, 6, 5);

        final var count = set.asSequence().count();

        assertEquals(6L, count);
    }

    @Test
    void testSequenceToLongSet() {
        LongMutableSetX longs = LongMutableSetX.empty();
        longs.addAll(LongRange.of(-10_000, 9_000));
        longs.addAll(LongRange.of(1_000, 90_000));
        longs.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        longs.add(Long.MIN_VALUE);

        final var evenLongs = longs.asSequence()
                .filter(LongX::isEven)
                .collect(LongMutableSetX::empty, LongMutableSetX::add);

        assertAll(
                () -> assertTrue(longs.containsAll(evenLongs)),
                () -> assertEquals(50_001, evenLongs.size())
        );
    }

}
