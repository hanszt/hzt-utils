package hzt.sequences.primitives;

import hzt.collections.MutableListX;
import hzt.collections.primitives.DoubleListX;
import hzt.ranges.DoubleRange;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DoubleSequenceTest {

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

        It.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        final var expected = new double[]{9999.689416376881, 9996.547823723291, 9993.4062310697, 9990.26463841611, 9987.12304576252};

        assertAll(
                () -> assertArrayEquals(expected, sorted)
        );
    }

    @Test
    void testZipDoubleSequenceWithDoubleArray() {
        double[] array = {1, 2, 3, 4, 5, 6};

        final var zipped = DoubleSequence.of(array)
                .zip(Double::sum, 1, 2, 3, 4)
                .toArray();

        It.println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new double[]{2, 4, 6, 8}, zipped);
    }

    @Test
    void testZipDoubleSequenceWithIterableOfDouble() {
        List<Double> list = MutableListX.of(1., 2., 3., 4., 5., 6.);
        double[] array = {1, 2, 3, 4, 5, 6, 7};

        final var zipped = DoubleSequence.of(array)
                .zip(Double::sum, list)
                .toArray();

        It.println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new double[]{2, 4, 6, 8, 10, 12}, zipped);
    }

    @Test
    void testWindowedDoubleSequence() {
        double[] array = {1, 2, 3, 4, 5, 6, 7};

        final var windowed = DoubleSequence.of(array)
                .windowed(5)
                .map(DoubleListX::toArray)
                .toTypedArray(double[][]::new);

        Sequence.of(windowed).map(Arrays::toString).forEach(It::println);

        assertEquals(3, windowed.length);
    }

    @Test
    void testWindowedDoubleSequenceWindowReduced() {
        double[] array = {1, 2, 3, 4, 5, 6, 7};

        final var sums = DoubleSequence.of(array)
                .windowed(3, DoubleListX::sum)
                .toArray();

        DoubleSequence.of(sums).forEachDouble(It::println);

        assertArrayEquals(new double[] {6, 9, 12, 15, 18}, sums);
    }

    @Test
    void testPartialWindowedDoubleSequence() {
        double[] array = {1, 2, 3, 4, 5, 6, 7};

        final var windows = DoubleSequence.of(array)
                .windowed(3, 2, true)
                .map(DoubleListX::toArray)
                .toTypedArray(double[][]::new);

        Sequence.of(windows).map(Arrays::toString).forEach(It::println);

        assertEquals(4, windows.length);
    }

    @Test
    void testPartialWindowedDoubleSequenceWindowReduced() {
        double[] array = {1, 2, 3, 4, 5, 6, 7};

        final var sums = DoubleSequence.of(array)
                .windowed(3, 2, true, DoubleListX::sum)
                .toArray();

        DoubleSequence.of(sums).forEachDouble(It::println);

        assertArrayEquals(new double[] {6, 12, 18, 7}, sums);
    }

    @Test
    void testWindowedLargeDoubleSequence() {
        final var sums = DoubleSequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, DoubleListX::sum)
                .toArray();

        final var sums2 = Sequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, s -> s.sumOfDoubles(It::asDouble))
                .mapToDouble(It::asDouble)
                .toArray();

        assertAll(
                () -> assertEquals(19981, sums.length),
                () -> assertArrayEquals(sums, sums2)
        );

    }

    @Test
    void testMapMulti() {
        final var doubles = DoubleSequence.generate(Math.PI, d -> d + Math.PI)
                .takeWhile(d -> d < 1_000 * Math.PI)
                .mapMulti(this::doWork)
                .toArray();

        final var firstFour = DoubleSequence.of(doubles).take(4).mapToObj(this::rounded);

        double[] expected = {Math.PI, Math.PI + .1, Math.PI + .2, Math.PI + .3};

        final var expectedFirstFour = DoubleSequence.of(expected).mapToObj(this::rounded);

        assertAll(
                () -> assertEquals(10000, doubles.length),
                () -> assertIterableEquals(expectedFirstFour, firstFour)
        );
    }

    String rounded(double d) {
        return String.format("%.8f", d);
    }

    private void doWork(double input, DoubleConsumer consumer) {
        double output = input;
        int counter = 0;
        while (counter < 10) {
            consumer.accept(output);
            output += .1;
            counter++;
        }
    }

}
