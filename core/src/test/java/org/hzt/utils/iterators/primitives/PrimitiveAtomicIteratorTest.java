package org.hzt.utils.iterators.primitives;

import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.junit.jupiter.api.Test;

import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveAtomicIteratorTest {

    @Test
    void testForEachRemaining() {
        IntMutableListX listX = IntMutableListX.empty();

        IntListX.of(1, 3, 5, 7, 8, 9).atomicIterator().forEachRemaining((IntConsumer) listX::add);

        assertEquals(IntListX.of(1, 3, 5, 7, 8, 9), listX);
    }

}
