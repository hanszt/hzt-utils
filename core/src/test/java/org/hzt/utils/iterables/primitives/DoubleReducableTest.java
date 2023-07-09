package org.hzt.utils.iterables.primitives;

import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleReducableTest {

    @Test
    void testReduceDouble() {
        final var sum = DoubleSequence.iterate(0, d -> d + .1)
                .map(Math::sin)
                .take(1_000_000)
                .reduce(0.0, Double::sum);

        assertThat(sum, 1e10).isCloseTo(20.0, offset(0.1)).isCloseTo(20.0, offset(0.1));
    }

    @Test
    void testDoubleReducableReduceTwo() {
        final var pair = DoubleSequence.iterate(1.0, d -> d + .1)
                .take(1_000_000)
                .reduceToTwo(0.0, Double::sum, 1.0, DoubleX::times, Pair::of);

        assertAll(
                () -> assertEquals(5.000094999996119E10 , pair.first()),
                () -> assertEquals(Double.POSITIVE_INFINITY, pair.second())
        );
    }

}
