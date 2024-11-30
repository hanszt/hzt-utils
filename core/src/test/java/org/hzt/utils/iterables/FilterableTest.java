package org.hzt.utils.iterables;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterableTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterableTest.class);

    @Test
    void testFilterNotTo() {
        final var integers = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filterNotTo(LinkedBlockingDeque::new, IntX::isEven);

        integers.forEach(it -> LOGGER.trace("{}", it));

        integers.forEach(i -> assertTrue(IntX.isOdd(i)));
    }
}
