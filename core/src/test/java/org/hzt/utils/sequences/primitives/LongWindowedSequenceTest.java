package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongWindowedSequenceTest {

    @Test
    void testVariableSizedLongSequence() {
        final ListX<LongList> chunks = IntSequence.iterate(0, Generator::sawTooth)
                .mapToLong(It::asLong)
                .chunked(1, Generator::sawTooth)
                .take(100)
                .toListX();

        chunks.forEach(It::println);

        assertEquals(5, chunks.count(chunk -> chunk.size() == 1));
    }
}
