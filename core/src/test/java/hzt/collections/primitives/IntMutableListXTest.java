package hzt.collections.primitives;

import hzt.sequences.primitives.IntSequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntMutableListXTest {

    @Test
    void testDifferentMethods() {
        final IntMutableListX ints = IntMutableListX.empty();
        assertTrue(ints.isEmpty());
        ints.add(1);
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(1, ints.size())
        );
        ints.clear();
        ints.add(2);
        ints.add(5);
        ints.add(7);
        final int l = ints.removeFirst();
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, ints.size())
        );
        It.println("ints = " + ints);
        final int l2 = ints.removeAt(0);
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(5L, l2),
                () -> assertEquals(1, ints.size())
        );

        ints.forEach(It::println);
    }

    @Test
    void testCopyConstructor() {
        final IntMutableListX ints = IntMutableListX.empty();
        ints.add(2);
        ints.add(-43);
        ints.add(1231);
        IntSequence.generate(0, l -> l + 5)
                .take(10_000_000)
                .forEachInt(ints::add);

        final IntMutableListX intsCopy = IntMutableListX.of(ints);

        assertAll(
                () -> assertEquals(10_000_003, ints.size()),
                () -> assertEquals(ints, intsCopy)
        );
    }
}
