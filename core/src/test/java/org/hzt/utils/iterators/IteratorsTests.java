package org.hzt.utils.iterators;

import org.hzt.test.assertions.Assertions;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IteratorsTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(IteratorsTests.class);

    @Test
    void testGeneratingIterator() {
        final Sequence<String> strings = () -> Iterators.generatorIterator(() -> "h", s1 -> s1 + s1);
        for (final var s : strings) {
            final var length = s.length();
            LOGGER.atDebug().setMessage(() -> "s = " + s).log();
            Assertions.assertThat(length).is(this::isPowerOfTwo);
            if (length > 1000) {
                break;
            }
        }
        final var count = strings.takeWhile(s -> s.length() < 32).count();
        assertEquals(5, count);
    }

    private boolean isPowerOfTwo(final int integer) {
        return integer > 0 && ((integer & (integer - 1)) == 0);
    }

    @Test
    void testIteratorChainIteratorObjectCanOnlyBeTraversedOnce() {
        final var iterator = Iterators.takeWhileIterator(
                Iterators.filteringIterator(
                        Iterators.generatorIterator(() -> "|", s -> s + "\\"),
                        s -> s.length() % 2 == 0, true), s -> s.length() < 100, false);

        final Iterable<String> strings = () -> iterator;

        assertThat(iterator).hasNext();

        strings.forEach(s -> LOGGER.debug("{}", s));

        assertThat(iterator).isExhausted();
    }

    @Test
    void testDistinctIteratorForEachRemaining() {
        final var distinctSequence = Sequence.of(2, 2, 3, 4, 4, 5).distinct();

        final List<Integer> list = new ArrayList<>();
        final var iterator = distinctSequence.iterator();
        iterator.forEachRemaining(list::add);

        LOGGER.atDebug().setMessage(() -> "list = " + list).log();

        assertEquals(list, distinctSequence.toList());
    }
}
