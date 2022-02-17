package hzt.sequences;

import hzt.collections.ListView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformingIndexedSequenceTest {

    @Test
    void testSequenceMapIndexed() {
        final ListX<String> strings = ListView.of("test", "map", "filter", "reduce");

        final ListX<String> result = strings.asSequence()
                .mapIndexed((index, value) -> value + index)
                .toListView();

        assertEquals(ListView.of("test0", "map1", "filter2", "reduce3"), result);
    }
}
