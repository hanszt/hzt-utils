package hzt.collections.primitives;

import hzt.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleMutableListXTest {

    @Test
    void testDifferentMethods() {
        final var doubles = DoubleMutableListX.empty();

        assertTrue(doubles.isEmpty());
        doubles.add(Math.PI);
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(1, doubles.size())
        );
        doubles.clear();
        doubles.add(2);
        doubles.add(7);
        doubles.add(Math.E);
        final var l = doubles.removeFirst();
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, doubles.size())
        );
        System.out.println("doubles = " + doubles);
        final var l2 = doubles.removeLast();
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(Math.E, l2),
                () -> assertEquals(1, doubles.size())
        );
    }

    @Test
    void testCopyConstructor() {
        final var doubles = DoubleMutableListX.empty();
        doubles.add(2);
        doubles.add(-43);
        doubles.add(Math.E);
        DoubleSequence.generate(0, l -> l + 5)
                .take(10_000_000)
                .forEachDouble(doubles::add);

        final var doublesCopy = DoubleMutableListX.of(doubles);

        assertAll(
                () -> assertEquals(10_000_003, doubles.size()),
                () -> assertEquals(doubles, doublesCopy)
        );
    }
}
