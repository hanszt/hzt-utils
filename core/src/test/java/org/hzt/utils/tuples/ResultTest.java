package org.hzt.utils.tuples;

import org.hzt.utils.It;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import static java.util.function.Predicate.not;
import static org.hzt.utils.tuples.Result.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultTest {

    @Test
    void testStreamHandlingExceptionsUsingEither() {

        final var results = IntRange.of(0, 100)
                .boxed()
                .plus(Sequence.of(null, null, 3))
                .map(catching(ResultTest::throwingWhenLargerThan20Times2))
                .onEach(It::println)
                .filter(not(Result::hasError))
                .toList();

        assertEquals(22, results.size());
    }

    private static int throwingWhenLargerThan20Times2(int v1) throws MyCheckedException {
        final var THROWING_BOUND = 20;
        if (v1 > THROWING_BOUND) {
            throw new MyCheckedException();
        }
        return v1 * 2;
    }

    private static class MyCheckedException extends Exception {

    }

}
