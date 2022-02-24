package hzt.iterables.primitives;

import hzt.collections.primitives.IntListX;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntReducableTest {

    @Test
    void testReduceTwo() {
        final IntListX integers = IntListX.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        integers.forEach(It::println);

        final Pair<Integer, Integer> pair = integers
                .reduceTwo(1, (acc, next) -> acc * next,
                        0, Integer::sum,
                        Pair::of);

        assertAll(
                () -> assertEquals(362880, pair.first()),
                () -> assertEquals(45, pair.second())
        );
    }
}
