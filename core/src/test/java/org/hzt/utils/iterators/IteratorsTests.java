package org.hzt.utils.iterators;

import org.hzt.utils.It;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IteratorsTests {

    @Test
    void testGeneratingIterator() {
        final Sequence<String> strings = () -> Iterators.generatorIterator(() -> "h", s1 -> s1 + s1);
        for (final String s : strings) {
            final int length = s.length();
            It.println("s = " + s);
            assertTrue(() -> isPowerOfTwo(length));
            if (length > 1000) {
                break;
            }
        }
        final long count = strings.takeWhile(s -> s.length() < 32).count();
        assertEquals(5, count);
    }

    private boolean isPowerOfTwo(final int integer) {
        return integer > 0 && ((integer & (integer - 1)) == 0);
    }

    @Test
    void testIteratorChainIteratorObjectCanOnlyBeTraversedOnce() {
        final Iterator<String> iterator =
                Iterators.takeWhileIterator(
                        Iterators.filteringIterator(
                                Iterators.generatorIterator(() -> "|", s -> s + "\\"),
                                s -> IntX.of(s.length()).isEven(), true), s -> s.length() < 100, false);

        final Iterable<String> strings = () -> iterator;

        assertTrue(iterator.hasNext());

        strings.forEach(It::println);

        assertFalse(iterator.hasNext());
    }

    @Test
    void testDistinctIteratorForEachRemaining() {
        final Sequence<Integer> distinctSequence = Sequence.of(2, 2, 3, 4, 4, 5).distinct();

        final List<Integer> list = new ArrayList<>();
        final Iterator<Integer> iterator = distinctSequence.iterator();
        iterator.forEachRemaining(list::add);

        System.out.println("list = " + list);

        assertEquals(list, distinctSequence.toList());
    }
}
