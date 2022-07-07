package org.hzt.utils.spined_buffers;

import org.junit.jupiter.api.Test;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpinedBufferTest {

    @Test
    void testIntBufferInitCapacity() {
        SpinedBuffer.OfInt intBuffer = new SpinedBuffer.OfInt(10);
        intBuffer.accept(1);
        final long count = StreamSupport.intStream(intBuffer.spliterator(), false).count();

        assertEquals(1L, count);
    }

}
