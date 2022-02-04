package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableLinkedSetXTest {

    @Test
    void testMutableLinkedEmptySetIsEmpty() {
        assertTrue(MutableLinkedSetX.empty()::isEmpty);
    }

    @Test
    void testMutableLinkedSetOfIterableMaintainsOrder() {
        final var bookList = TestSampleGenerator.createBookList();

        var set = MutableLinkedSetX.of(bookList);

        assertEquals(bookList.get(0), set.first());
    }

}
