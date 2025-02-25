package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedIterableTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexedIterableTest.class);

    @Test
    void testForEachIndexed() {
        final List<IndexedValue<Integer>> list = new ArrayList<>();

        IntRange.closed(1, 100)
                .filter(IntX::isEven)
                .onEach(it -> LOGGER.trace("{}", it))
                .boxed()
                .forEachIndexedValue(list::add);

        LOGGER.atDebug().setMessage(() -> "list = " + list).log();

        assertEquals(50, list.size());
    }

    @Test
    void testIndexedSpliterator() {
        final var indexedSpliterator = Sequence.of("This", "is", "a", "spliterator", "test")
                .indexedSpliterator();

        final var integers = StreamSupport.stream(indexedSpliterator, false)
                .filter(i -> i.index() % 2 == 0)
                .map(IndexedValue::value)
                .toArray(String[]::new);

        final var expected = new String[]{"This", "a", "test"};

        assertArrayEquals(expected, integers);
    }

    @Test
    void testForEachIndex() {
        final var indices = IntMutableList.empty();

        ListX.of("This", "is", "a", "test").forEachIndex(indices::add);

        assertEquals(IntMutableList.of(0, 1, 2, 3), indices);
    }
}
