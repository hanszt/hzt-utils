package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedIterableTest {

    @Test
    void testForEachIndexed() {
        List<IndexedValue<Integer>> list = new ArrayList<>();

        IntRange.closed(1, 100)
                .filter(IntX::isEven)
                .onEach(It::println)
                .boxed()
                .forEachIndexedValue(list::add);

        It.println("list = " + list);

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
        final IntMutableList indices = IntMutableList.empty();

        ListX.of("This", "is", "a", "test").forEachIndex(indices::add);

        assertEquals(IntMutableList.of(0, 1, 2, 3), indices);
    }
}
