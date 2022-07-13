package org.hzt.utils.iterators.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.junit.jupiter.api.Test;

import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveAtomicIteratorTest {

    @Test
    void testForEachRemaining() {
        IntMutableList listX = IntMutableList.empty();

        IntList.of(1, 3, 5, 7, 8, 9).atomicIterator().forEachRemaining((IntConsumer) listX::add);

        assertEquals(IntList.of(1, 3, 5, 7, 8, 9), listX);
    }

}
