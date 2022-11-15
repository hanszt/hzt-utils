package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LongReducableTest {

    @Test
    void testReduceToTwo() {
        final var sumAndMax = LongSequence.iterate(1, l -> 2 * l)
                .takeWhile(l -> l < 2e10)
                .onEach(It::println)
                .reduceToTwo(Long::sum, Long::max, Pair::of)
                .orElseThrow();

        assertAll(
                () -> assertEquals(34359738367L, sumAndMax.first()),
                () -> assertEquals(17179869184L, sumAndMax.second())
        );
    }

}
