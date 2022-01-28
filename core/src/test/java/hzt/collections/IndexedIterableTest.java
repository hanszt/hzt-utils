package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexedIterableTest {

    @Test
    void testForEachIndexed() {
        List<IndexedValue<Integer>> list = new ArrayList<>();

        IterableX.rangeClosed(1, 100, this::isEven).forEachIndexedValue(list::add);

        System.out.println("list = " + list);

        assertEquals(50, list.size());
    }

    private boolean isEven(int i) {
        return i % 2 == 0;
    }
}
