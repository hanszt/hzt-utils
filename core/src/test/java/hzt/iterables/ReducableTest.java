package hzt.iterables;

import hzt.collections.SetX;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReducableTest {

    @Test
    void testSingle() {
        final SetX<Integer> singleton = SetX.of(10);
        final Integer single = singleton.single();
        assertEquals(10, single);
    }

    @Test
    void testSingleCallOnEmptyIterableYieldsNoSuchElementException() {
        final SetX<Object> set = SetX.empty();
        assertThrows(NoSuchElementException.class, set::single);
    }

    @Test
    void testSingleCallOnIterableHavingMoreThanOneElementYieldsIllegalArgumentException() {
        final SetX<Integer> set = SetX.of(10, 9);
        assertThrows(IllegalArgumentException.class, set::single);
    }

}
