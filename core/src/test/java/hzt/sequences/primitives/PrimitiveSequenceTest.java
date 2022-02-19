package hzt.sequences.primitives;

import hzt.numbers.BigDecimalX;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveSequenceTest {

    @Test
    void testLongRangeToListGetBigDecimalAverage() {
        final var longs = LongSequence.of(0, 1000).boxed().toListX();

        final var average = longs.toBigDecimalXAverage(BigDecimalX::of);

        It.println("average = " + average);

        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        final var length = average.toStringX().filter(c -> c != '.').length();

        Locale.setDefault(defaultLocale);

        assertEquals(5, length);
    }

    @Test
    void testRangeFrom10To10YieldsEmptyRange() {
        assertAll(
                () -> assertTrue(IntSequence.of(10, 10).none()),
                () -> assertTrue(LongSequence.of(10, 10).none()),
                () -> assertTrue(DoubleSequence.of(10, 10).none()));
    }

}
