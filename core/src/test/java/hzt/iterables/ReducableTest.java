package hzt.iterables;

import hzt.collections.SetX;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReducableTest {

    @Test
    void testSingle() {
        final var singleton = SetX.of(10);
        final var single = singleton.single();
        assertEquals(10, single);
    }

    @Test
    void testSingleCallOnEmptyIterableYieldsNoSuchElementException() {
        final var set = SetX.empty();
        assertThrows(NoSuchElementException.class, set::single);
    }

    @Test
    void testSingleCallOnIterableHavingMoreThanOneElementYieldsIllegalArgumentException() {
        final var set = SetX.of(10, 9);
        assertThrows(IllegalArgumentException.class, set::single);
    }

}
