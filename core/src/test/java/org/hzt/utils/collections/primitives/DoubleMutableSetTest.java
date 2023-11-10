package org.hzt.utils.collections.primitives;

import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleMutableSetTest {

    @Test
    void testDoubleSetSize() {
        final DoubleMutableSet set = DoubleMutableSet.empty();
        set.addAll(DoubleList.of(-1, -2231, 0, 3, 3, 4, 5, 6, 5));

        assertEquals(7, set.size());
    }

    @Test
    void testCountElementsInDoubleSetThroughSequence() {
        final DoubleMutableSet set = DoubleMutableSet.of(1, 2, 3, 3, 4, 5, 6, 5);

        final long count = set.asSequence().count();

        assertEquals(6L, count);
    }

    @Test
    void testSequenceToDoubleSet() {
        final DoubleMutableSet doubles = DoubleMutableSet.empty();
        doubles.addAll(DoubleSequence.iterate(-10_000.0, d -> d + Math.E).take(90_000));
        doubles.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, Math.PI, -10_000.0);
        doubles.add(100_000.2342);
        doubles.add(Double.POSITIVE_INFINITY);
        doubles.add(Double.NEGATIVE_INFINITY);

        final DoubleMutableSet evenDoubles = doubles.asSequence()
                .filter(DoubleX::isFinite)
                .collect(DoubleMutableSet::empty, DoubleMutableSet::add);

        assertAll(
                () -> assertTrue(doubles.containsAll(evenDoubles)),
                () -> assertEquals(90_011, evenDoubles.size())
        );
    }

}
