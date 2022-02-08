package hzt.ranges;

import hzt.numbers.BigDecimalX;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberRangeTest {

    @Test
    void testLongRangeToArray() {
        final var longs = LongRange.of(0, 1000).toArrayX();

        final var average = longs.toBigDecimalAverage(BigDecimalX::of);

        System.out.println("average = " + average);

        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final var length = average.toStringX().filter(c -> c != '.').length();

        Locale.setDefault(defaultLocale);

        assertEquals(5, length);
    }

}
