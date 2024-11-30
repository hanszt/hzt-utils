package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongWindowedSequenceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LongWindowedSequenceTest.class);

    @Test
    void testVariableSizedLongSequence() {
        final var chunks = IntSequence.iterate(0, Generator::sawTooth)
                .mapToLong(It::asLong)
                .chunked(1, Generator::sawTooth)
                .take(100)
                .toListX();

        chunks.forEach(it -> LOGGER.trace("{}", it));

        assertEquals(5, chunks.count(chunk -> chunk.size() == 1));
    }
}
