package org.hzt.utils.collectors.primitves;

import org.junit.jupiter.api.Test;

import static java.lang.Math.E;
import static org.hzt.utils.sequences.primitives.DoubleSequence.generate;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleCollectorsTest {

    @Test
    void testCollectToDoubleList() {
        final var doubles = generate(1, d -> d + E)
                .take(10)
                .onEach(System.out::println)
                .collect(DoubleCollectors.toList());

        final var last = doubles.last();

        assertAll(
                () -> assertEquals(10, doubles.size()),
                () -> assertEquals(25.464536456131405, last)
        );
    }

}
