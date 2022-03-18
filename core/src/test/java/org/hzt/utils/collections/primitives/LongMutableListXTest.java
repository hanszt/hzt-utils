package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.It;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongMutableListXTest {

    @BeforeAll
    static void setup() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
    }

    @Test
    void testDifferentMethods() {
        final LongMutableListX longs = LongMutableListX.empty();
        assertTrue(longs.isEmpty());
        longs.add(1);
        assertAll(
                () -> assertFalse(longs.isEmpty()),
                () -> assertEquals(1, longs.size())
        );
        longs.clear();
        longs.add(2);
        longs.add(5);
        longs.add(7);
        final long l = longs.removeFirst();
        assertAll(
                () -> assertFalse(longs.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, longs.size())
        );
        It.println("longs = " + longs);
        final long l2 = longs.removeAt(0);
        assertAll(
                () -> assertFalse(longs.isEmpty()),
                () -> assertEquals(5L, l2),
                () -> assertEquals(1, longs.size())
        );
    }

    @Test
    void testCopyConstructor() {
        final LongMutableListX longs = LongMutableListX.empty();
        longs.add(2);
        longs.add(-43);
        longs.add(1231);
        LongSequence.generate(0, l -> l + 5)
                .take(10_000_000)
                .forEachLong(longs::add);

        final LongMutableListX longsCopy = LongMutableListX.of(longs);

        assertAll(
                () -> assertEquals(10_000_003, longs.size()),
                () -> assertEquals(longs, longsCopy)
        );
    }

    @Test
    void testSet() {
        final LongMutableListX list = LongMutableListX.of(1, 2, 3, 4, 5, -1, 3, 6, 3, 2, 5);
        list.set(4, 14);
        final long value = list.get(4);
        assertEquals(14L, value);
    }
}
