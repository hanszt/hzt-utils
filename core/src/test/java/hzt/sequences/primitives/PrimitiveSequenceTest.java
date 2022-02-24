package hzt.sequences.primitives;

import hzt.collections.ListView;
import hzt.numbers.BigDecimalX;
import hzt.ranges.DoubleRange;
import hzt.ranges.IntRange;
import hzt.ranges.LongRange;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveSequenceTest {

    @Test
    void testLongRangeToListGetBigDecimalAverage() {
        final ListX<Long> longs = LongSequence.of(0, 1000).boxed().toListX();

        final BigDecimalX average = longs.toBigDecimalXAverage(BigDecimalX::of);

        It.println("average = " + average);

        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final int length = average.toStringX().filter(c -> c != '.').length();

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
