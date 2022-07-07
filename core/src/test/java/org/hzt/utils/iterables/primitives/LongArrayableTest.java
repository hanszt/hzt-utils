package org.hzt.utils.iterables.primitives;

import org.junit.jupiter.api.Test;

import static org.hzt.utils.sequences.primitives.LongSequence.generate;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class LongArrayableTest {

    @Test
    void testToByteArray() {
        final byte[] bytes = generate(0, l -> ++l)
                .take(10)
                .toByteArray(l -> (byte) l);

        assertArrayEquals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, bytes);
    }

}
