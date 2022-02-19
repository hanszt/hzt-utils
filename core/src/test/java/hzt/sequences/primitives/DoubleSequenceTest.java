package hzt.sequences.primitives;

import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleSequenceTest {

    @Test
    void testRange() {
        final var stats = DoubleSequence.of(0, 100, .01).stats();

        assertAll(
                () -> assertEquals(10_000, stats.getCount()),
                () -> assertEquals("49.995", String.format(Locale.ENGLISH, "%.3f", stats.getAverage())),
                () -> assertEquals("99.99", String.format(Locale.ENGLISH, "%.2f", stats.getMax()))
        );
    }

    @Test
    void doubleRangeFromDoubleArray() {
        double[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = DoubleStream.of(array)
                .mapToLong(d -> (long) d)
                .filter(l -> l > 3)
                .toArray();


        final var longs = DoubleSequence.of(array)
                .mapToLong(It::doubleAsLong)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void testSortedDescending() {
        final var sorted = DoubleSequence.generate(0, d -> d + Math.PI)
                .takeWhile(d -> d < 10_000)
                .sortedDescending()
                .take(5)
                .toArray();

        System.out.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        final var expected = new double[]{9999.689416376881, 9996.547823723291, 9993.4062310697, 9990.26463841611, 9987.12304576252};

        assertAll(
                () -> assertArrayEquals(expected, sorted)
        );
    }

}
