package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleCollectableTest {

    @Test
    void testDoubleSequenceToMutableSet() {
        final var doubles = DoubleSequence.iterate(Double.MIN_VALUE, d -> d + Math.PI)
                .take(10)
                .plus(Math.E, Math.E, DoubleX.GOLDEN_RATIO)
                .toMutableSet();

        doubles.forEachDouble(It::println);

        final var actual = DoubleX.toRoundedString(doubles.last(), 5, Locale.ENGLISH);

        assertAll(
                () -> assertEquals(12, doubles.size()),
                () -> assertEquals("1.61803", actual)
        );
    }
}
