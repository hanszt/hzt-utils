package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableLinkedSetTest {

    @Test
    void testMutableLinkedEmptySetIsEmpty() {
        assertTrue(MutableLinkedSet.empty()::isEmpty);
    }

    @Test
    void testMutableLinkedSetOfIterableMaintainsOrder() {
        final var bookList = TestSampleGenerator.createBookList();

        var set = MutableLinkedSet.of(bookList);

        assertEquals(bookList.get(0), set.first());
    }

}
