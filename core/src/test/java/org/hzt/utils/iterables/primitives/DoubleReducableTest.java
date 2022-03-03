package org.hzt.utils.iterables.primitives;

import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleReducableTest {

    @Test
    void testReduceDouble() {
        final double sum = DoubleSequence.generate(0, d -> d + .1)
                .map(Math::sin)
                .take(1_000_000)
                .reduce(0.0, Double::sum);

        assertEquals(19.959070699659645, sum);
    }

}
