package org.hzt.utils.iterators;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IteratorTests {

    @Test
    void testGeneratingIterator() {
        Iterator<String> iterator = GeneratorIterator.of(() -> "h", s -> s + s);
        Iterable<String> strings = () -> iterator;
        for (String s : strings) {
            final var length = s.length();
            It.println("s = " + s);
            assertTrue(() -> isPowerOfTwo(length));
            if (length > 1000) {
                break;
            }
        }
    }

    private boolean isPowerOfTwo(int integer) {
        return integer > 0 && ((integer & (integer - 1)) == 0);
    }

    @Test
    void testIteratorChain() {
        Iterator<String> iterator =
                TakeWhileIterator.of(
                        FilteringIterator.of(
                                GeneratorIterator.of(() -> "|", s -> s + "\\"),
                                s -> IntX.of(s.length()).isEven(), true),
                        s -> s.length() < 100);

        Iterable<String> strings = () -> iterator;

        assertTrue(iterator.hasNext());

        strings.forEach(It::println);

        assertFalse(iterator.hasNext());
    }
}