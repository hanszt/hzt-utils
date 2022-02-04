package hzt.collections;

import hzt.ranges.IntRange;
import hzt.utils.It;
import hzt.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedIterableTest {

    @Test
    void testForEachIndexed() {
        List<IndexedValue<Integer>> list = new ArrayList<>();

        IntRange.closed(1, 100)
                .filter(this::isEven)
                .onEach(It::println)
                .forEachIndexedValue(list::add);

        It.println("list = " + list);

        assertEquals(50, list.size());
    }

    private boolean isEven(int i) {
        return i % 2 == 0;
    }
}
