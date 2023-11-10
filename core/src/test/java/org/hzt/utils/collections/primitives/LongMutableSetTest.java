package org.hzt.utils.collections.primitives;

import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.LongRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongMutableSetTest {

    @Test
    void testLongSetSize() {
        final LongMutableSet set = LongMutableSet.empty();
        set.addAll(LongList.of(-1, -2231, 0, 3, 3, 4, 5, 6, 5));

        assertEquals(7, set.size());
    }

    @Test
    void testCountElementsInLongSetThroughSequence() {
        final LongMutableSet set = LongMutableSet.of(1, 2, 3, 3, 4, 5, 6, 5);

        final long count = set.asSequence().count();

        assertEquals(6L, count);
    }

    @Test
    void testSequenceToLongSet() {
        final LongMutableSet longs = LongMutableSet.empty();
        longs.addAll(LongRange.of(-10_000, 9_000));
        longs.addAll(LongRange.of(1_000, 90_000));
        longs.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        longs.add(Long.MIN_VALUE);

        final LongMutableSet evenLongs = longs.asSequence()
                .filter(LongX::isEven)
                .collect(LongMutableSet::empty, LongMutableSet::add);

        assertAll(
                () -> assertTrue(longs.containsAll(evenLongs)),
                () -> assertEquals(50_001, evenLongs.size())
        );
    }

    @Test
    void testRemoveAll() {
        final LongMutableSet longs = LongMutableSet.of(2, 4, 3, 5, 4, 6, 5, 7, 6, 5, 4, 4);
        final boolean removeAll = longs.removeAll(2, 3, 4, 3, 5);

        assertAll(
                () -> assertFalse(removeAll),
                () -> assertEquals(LongMutableSet.of(7, 6), longs)
        );
    }

    @Test
    void testNonEqualLongSetYieldsEqualsIsFalse() {
        final LongMutableSet set1 = LongMutableSet.of(1, 2, 4, 5);
        final LongMutableSet set2 = LongMutableSet.of(1, 2, 4, 6);
        assertNotEquals(set1, set2);
    }

    @Test
    void testIntSetAndLongSetAreNotEqual() {
        final LongMutableSet set1 = LongMutableSet.of(1, 2, 4, 5);
        final IntMutableSet set2 = IntMutableSet.of(1, 2, 4, 5);
        assertNotEquals(set1, set2);
    }

}
