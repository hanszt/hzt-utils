package org.hzt.utils.iterables.primitives;

import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleReducableTest {

    @Test
    void testReduceDouble() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final double sum = DoubleSequence.iterate(0, d -> d + .1)
                .map(Math::sin)
                .take(1_000_000)
                .reduce(0.0, Double::sum);

        assertEquals("19.96", DoubleX.toRoundedString(sum));

        Locale.setDefault(defaultLocale);
    }

    @Test
    void testDoubleReducableReduceTwo() {
        final Pair<Double, Double> pair = DoubleSequence.iterate(1.0, d -> d + .1)
                .take(1_000_000)
                .reduceToTwo(0.0, Double::sum, 1.0, DoubleX::times, Pair::of);

        assertAll(
                () -> assertEquals(5.000094999996119E10 , pair.first()),
                () -> assertEquals(Double.POSITIVE_INFINITY, pair.second())
        );
    }

}
