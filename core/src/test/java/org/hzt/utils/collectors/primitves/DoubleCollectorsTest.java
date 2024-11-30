package org.hzt.utils.collectors.primitves;

import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.E;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleCollectorsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoubleCollectorsTest.class);

    @Test
    void testCollectToDoubleList() {
        final var doubles = DoubleSequence.iterate(1, d -> d + E)
                .take(10)
                .onEach(it -> LOGGER.trace("{}", it))
                .collect(DoubleCollectors.toList());

        final var last = doubles.last();

        assertAll(
                () -> assertEquals(10, doubles.size()),
                () -> assertEquals(25.464536456131405, last)
        );
    }

}
