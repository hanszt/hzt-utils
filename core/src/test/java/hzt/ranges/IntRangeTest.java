package hzt.ranges;

import hzt.collections.MutableList;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntRangeTest {

    @Test
    void testSteppedIntRange() {
        var list = MutableList.<Integer>empty();
        for (int i : IntRange.until(15).step(4)) {
            It.println(i);
            list.add(i);
        }
        assertEquals(List.of(0, 4, 8, 12), list);
    }

    @Test
    void testDescendingSteppedIntRange() {
        var list = MutableList.<Integer>empty();
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

    @Test
    void testStats() {
        final var expected = IntStream.range(0, 100).summaryStatistics();

        final var actual = IntRange.of(0, 100).stats();

        It.println("actual = " + actual);

        assertAll(
                () -> assertEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void testIntRangeFromIntStream() {
        final var expected = IntStream.range(0, 100).summaryStatistics();

        final var actual = IntRange.of(IntStream.range(0, 100)).stats();

        It.println("actual = " + actual);

        assertAll(
                () -> assertEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void intRangeFromIntArray() {
        int[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = IntStream.of(array)
                .mapToLong(It::asLong)
                .filter(l -> l > 3)
                .toArray();


        final var longs = IntRange.of(array)
                .asLongRange(It::asLong)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }
}
