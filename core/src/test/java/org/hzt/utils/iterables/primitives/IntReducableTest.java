package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntReducableTest {

    @Test
    void testReduceTwo() {
        final IntListX integers = IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        integers.forEachInt(It::println);

        final Pair<Integer, Integer> pair = integers
                .reduceToTwo(1, (acc, next) -> acc * next,
                        0, Integer::sum,
                        Pair::of);

        assertAll(
                () -> assertEquals(362880, pair.first()),
                () -> assertEquals(45, pair.second())
        );
    }
}
