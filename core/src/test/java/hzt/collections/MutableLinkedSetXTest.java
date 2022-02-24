package hzt.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableLinkedSetXTest {

    @Test
    void testMutableLinkedEmptySetIsEmpty() {
        assertTrue(MutableLinkedSetX.empty()::isEmpty);
    }

    @Test
    void testMutableLinkedSetOfIterableMaintainsOrder() {
        final List<Book> bookList = TestSampleGenerator.createBookList();

        MutableLinkedSetX<Book> set = MutableLinkedSetX.of(bookList);

        assertEquals(bookList.get(0), set.first());
    }

}
