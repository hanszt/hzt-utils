package org.hzt.utils.iterators.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveAtomicIteratorTest {

    @Test
    void testForEachRemaining() {
        final var listX = IntMutableList.empty();

        IntList.of(1, 3, 5, 7, 8, 9).atomicIterator().forEachRemaining((IntConsumer) listX::add);

        assertEquals(IntMutableList.of(1, 3, 5, 7, 8, 9), listX);
    }

    @Test
    void testConsumerInstanceOfIntConsumer() {
        final Consumer<Integer> consumer = new IntegerConsumer();

        assertInstanceOf(IntConsumer.class, consumer);
    }

    private static class IntegerConsumer implements Consumer<Integer>, IntConsumer {

        private int value = 0;
        @Override
        public void accept(final Integer integer) {
            value = integer;
        }

        @Override
        public void accept(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
