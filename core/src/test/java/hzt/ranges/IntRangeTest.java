package hzt.ranges;

import hzt.collections.MutableListX;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntRangeTest {

    @Test
    void testSteppedIntRange() {
        var list = MutableListX.<Integer>empty();
        for (int i : IntRange.until(15).step(4)) {
            It.println(i);
            list.add(i);
        }
        assertEquals(List.of(0, 4, 8, 12), list);
    }

    @Test
    void testDescendingSteppedIntRange() {
        var list = MutableListX.<Integer>empty();
        for (int i : IntRange.from(100).downTo(20).step(5)) {
            It.println(i);
            list.add(i);
        }
        assertAll(
                () -> assertEquals(17, list.size()),
                () -> assertEquals(100, list.first())
        );
    }

    @Test
    void testDescendingIntRange() {
        final var integers = IntRange.from(100).downTo(20);
        assertAll(
                () -> assertEquals(81, integers.count()),
                () -> assertEquals(100, integers.first())
        );
    }

    @Test
    void testGetEmptyIntRangeWhenFromValueIsGreaterThanUntilValue() {
        assertEquals(0 , IntRange.from(100).until(0).count());
    }

}
