package org.hzt.utils.sequences.primitives;

import org.hzt.utils.numbers.BigDecimalX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.ranges.LongRange;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveSequenceTest {

    @Test
    void testLongRangeToListGetBigDecimalAverage() {
        final var longs = LongSequence.of(0, 1000).boxed().toListX();

        final var average = longs.bigDecimalAverageOf(BigDecimalX::of);

        It.println("average = " + average);

        var defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final var length = average.toStringX().filter(c -> c != '.').length();

        Locale.setDefault(defaultLocale);

        assertEquals(5, length);
    }

    @Test
    void testRangeFrom10To10YieldsEmptyRange() {
        assertAll(
                () -> assertTrue(IntRange.of(10, 10).none()),
                () -> assertTrue(LongRange.of(10, 10).none())
        );
    }

}
