package hzt.ranges;

import hzt.collections.ArrayX;
import hzt.numbers.BigDecimalX;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberRangeTest {

    @Test
    void testLongRangeToArray() {
        final ArrayX<Long> longs = LongRange.of(0, 1000).toArrayX();

        final BigDecimalX average = longs.toBigDecimalXAverage(BigDecimalX::of);

        System.out.println("average = " + average);

        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final int length = average.toStringX().filter(c -> c != '.').length();

        Locale.setDefault(defaultLocale);

        assertEquals(5, length);
    }

}
