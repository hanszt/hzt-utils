package org.hzt.utils.collections;

import org.hzt.test.TestSampleGenerator;
import org.junit.jupiter.api.Test;

import static org.hzt.test.assertions.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableSequencedSetXTest {

    @Test
    void testMutableLinkedEmptySetIsEmpty() {
        assertThat(MutableSequencedSetX.empty()).isEmpty();
    }

    @Test
    void testMutableLinkedSetOfIterableMaintainsOrder() {
        final var bookList = TestSampleGenerator.createBookList();

        final var set = MutableSequencedSetX.of(bookList);

        assertEquals(bookList.get(0), set.first());
    }

}
