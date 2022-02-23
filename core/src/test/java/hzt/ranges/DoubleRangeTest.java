package hzt.ranges;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleRangeTest {

    @Test
    void testRange() {
        final var stats = DoubleRange.closed(0, 100, .01).stats();

        assertAll(
                () -> assertEquals(10_000, stats.getCount()),
                () -> assertEquals("49.995", String.format(Locale.ENGLISH, "%.3f", stats.getAverage())),
                () -> assertEquals("99.99", String.format(Locale.ENGLISH, "%.2f", stats.getMax()))
        );
    }

    @Test
    void testClosedDoubleRange() {
        assertEquals(10.0, DoubleRange.closed(10, 10).boxed().single());
    }
}
