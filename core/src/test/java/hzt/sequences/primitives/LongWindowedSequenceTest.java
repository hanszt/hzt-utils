package hzt.sequences.primitives;

import hzt.test.Generator;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongWindowedSequenceTest {

    @Test
    void testVariableSizedLongSequence() {
        final var chunks = IntSequence.generate(0, Generator::sawTooth)
                .mapToLong(It::asLong)
                .chunked(1, Generator::sawTooth)
                .take(100)
                .toListX();

        chunks.forEach(It::println);

        assertEquals(5, chunks.count(chunk -> chunk.size() == 1));
    }
}
