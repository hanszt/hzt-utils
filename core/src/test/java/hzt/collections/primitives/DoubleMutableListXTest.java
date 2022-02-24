package hzt.collections.primitives;

import hzt.sequences.primitives.DoubleSequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleMutableListXTest {

    @Test
    void testDifferentMethods() {
        final DoubleMutableListX doubles = DoubleMutableListX.empty();

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
        final double l = doubles.removeFirst();
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, doubles.size())
        );
        It.println("doubles = " + doubles);
        final double l2 = doubles.removeLast();
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(Math.E, l2),
                () -> assertEquals(1, doubles.size())
        );
    }

    @Test
    void testCopyConstructor() {
        final DoubleMutableListX doubles = DoubleMutableListX.empty();
        doubles.add(2);
        doubles.add(-43);
        doubles.add(Math.E);
        DoubleSequence.generate(0, l -> l + 5)
                .take(10_000_000)
                .forEachDouble(doubles::add);

        final DoubleMutableListX doublesCopy = DoubleMutableListX.of(doubles);

        assertAll(
                () -> assertEquals(10_000_003, doubles.size()),
                () -> assertEquals(doubles, doublesCopy)
        );
    }
}
