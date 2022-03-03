package org.hzt.utils.iterables;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}
