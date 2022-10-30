package org.hzt.utils.sequences;

import org.hzt.utils.collections.ListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformingIndexedSequenceTest {

    @Test
    void testSequenceMapIndexed() {
        final var strings = ListX.of("test", "map", "filter", "reduce");

        final var result = strings.asSequence()
                .mapIndexed((index, value) -> value + index)
                .toListX();

        assertEquals(ListX.of("test0", "map1", "filter2", "reduce3"), result);
    }
}
