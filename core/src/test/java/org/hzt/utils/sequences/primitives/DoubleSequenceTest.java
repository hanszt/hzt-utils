package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

class DoubleSequenceTest {

    public static final String REPEATED_TEST_DISPLAY_NAME = RepeatedTest.CURRENT_REPETITION_PLACEHOLDER + "/" + RepeatedTest.TOTAL_REPETITIONS_PLACEHOLDER;

    @Test
    void doubleRangeFromDoubleArray() {
        final double[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2, Math.PI};

        final long[] expected = DoubleStream.of(array)
                .filter(d -> d > 3)
                .mapToLong(d -> (long) d)
                .toArray();


        final long[] actual = DoubleSequence.of(array)
                .filter(d -> d > 3)
                .mapToLong(It::doubleAsLong)
                .toArray();

        println(Arrays.toString(actual));

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4, 3}, actual),
                () -> assertArrayEquals(expected, actual)
        );
    }

    @Test
    void testDoubleSequencePlusArray() {
        final double[] array = {1, Math.PI, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final double[] result = DoubleSequence.of(1, 3, 2, 5, 4, 2)
                .plus(Math.E, 76, 5)
                .plus(DoubleList.of(array))
                .toArray();

        println(Arrays.toString(result));

        final double[] expected = new double[]{1, 3, 2, 5, 4, 2, Math.E, 76, 5, 1, Math.PI, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        assertAll(
                () -> assertEquals(21, result.length),
                () -> assertArrayEquals(expected, result)
        );
    }

    @Test
    void testSortedDescending() {
        final double[] sorted = DoubleSequence.generate(0, d -> d + Math.PI)
                .takeWhile(d -> d < 10_000)
                .sortedDescending()
                .take(5)
                .toArray();

        println("Arrays.toString(array) = " + Arrays.toString(sorted));

        final double[] expected = new double[]{9999.689416376881, 9996.547823723291, 9993.4062310697, 9990.26463841611, 9987.12304576252};

        assertAll(
                () -> assertArrayEquals(expected, sorted)
        );
    }

    @Test
    void testZipDoubleSequenceWithDoubleArray() {
        final double[] array = {1, 2, 3, 4, 5, 6};

        final double[] zipped = DoubleSequence.of(array)
                .zip(Double::sum, 1, 2, 3, 4)
                .toArray();

        println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new double[]{2, 4, 6, 8}, zipped);
    }

    @Test
    void testZipDoubleSequenceWithIterableOfDouble() {
        final List<Double> list = MutableListX.of(1., 2., 3., 4., 5., 6.);
        final double[] array = {1, 2, 3, 4, 5, 6, 7};

        final double[] zipped = DoubleSequence.of(array)
                .zip(Double::sum, list)
                .toArray();

        println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new double[]{2, 4, 6, 8, 10, 12}, zipped);
    }

    @Test
    void testWindowedDoubleSequence() {
        final double[] array = {1, 2, 3, 4, 5, 6, 7};

        final double[][] windowed = DoubleSequence.of(array)
                .windowed(5)
                .map(DoubleList::toArray)
                .toTypedArray(double[][]::new);

        Sequence.of(windowed).map(Arrays::toString).forEach(It::println);

        assertEquals(3, windowed.length);
    }

    @Test
    void testWindowedDoubleSequenceWindowReduced() {
        final double[] array = {1, 2, 3, 4, 5, 6, 7};

        final double[] sums = DoubleSequence.of(array)
                .windowed(3, DoubleList::sum)
                .toArray();

        DoubleSequence.of(sums).forEachDouble(It::println);

        assertArrayEquals(new double[] {6, 9, 12, 15, 18}, sums);
    }

    @Test
    void testPartialWindowedDoubleSequenceWindowReduced() {
        final double[] array = {1, 2, 3, 4, 5, 6, 7};

        final double[] sums = DoubleSequence.of(array)
                .windowed(3, 2, true, DoubleList::sum)
                .toArray();

        DoubleSequence.of(sums).forEachDouble(It::println);

        assertArrayEquals(new double[] {6, 12, 18, 7}, sums);
    }

    @Test
    @Timeout(value = 1_000, unit = TimeUnit.MILLISECONDS)
    void testWindowedLargeDoubleSequence() {
        final double[] sums = DoubleSequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, DoubleList::sum)
                .toArray();

       assertEquals(19981, sums.length);
    }

    @Test
    @Timeout(value = 1_000, unit = TimeUnit.MILLISECONDS)
    void testWindowedLargeBoxedDoubleSequence() {
        final double[] sums2 = Sequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, s -> s.doubleSumOf(It::asDouble))
                .toTypedArray(Double[]::new);

        assertEquals(19981, sums2.length);
    }

    @Test
    void testMapMulti() {
        final double[] doubles = DoubleSequence.generate(Math.PI, d -> d + Math.PI)
                .takeWhile(d -> d < 1_000 * Math.PI)
                .mapMulti(this::doWork)
                .toArray();

        final Sequence<String> firstFour = DoubleSequence.of(doubles).take(4).mapToObj(this::rounded);

        final double[] expected = {Math.PI, Math.PI + .1, Math.PI + .2, Math.PI + .3};

        final Sequence<String> expectedFirstFour = DoubleSequence.of(expected).mapToObj(this::rounded);

        assertAll(
                () -> assertEquals(100_000, doubles.length),
                () -> assertIterableEquals(expectedFirstFour, firstFour)
        );
    }

    String rounded(final double d) {
        return String.format("%.8f", d);
    }

    private void doWork(final double input, final DoubleConsumer consumer) {
        double output = input;
        int counter = 0;
        while (counter < 100) {
            consumer.accept(output);
            output += .1;
            counter++;
        }
    }

    @Test
    void testOnDoubleSequence() {
        final var nrOfCycles = 1_234;
        final var doubleMutableList = DoubleMutableList.empty();

        final double[] doubles = DoubleSequence.generate(0, d -> d + .1)
                .take(nrOfCycles)
                .onSequence(sequence -> sequence.boxed()
                        .step(200)
                        .onEach(doubleMutableList::add)
                        .forEach(It::println))
                .toArray();

        assertAll(
                () -> assertEquals(7, doubleMutableList.size()),
                () -> assertEquals(nrOfCycles, doubles.length)
        );
    }

    @Test
    void testGoldenRatioConvergence() {
        final double goldenRatio = (1 + Math.sqrt(5)) / 2;
        final int scale = 20;

        final String roundedGoldenRatio = DoubleX.toRoundedString(goldenRatio, scale);

        println("roundedGoldenRatio = " + roundedGoldenRatio);

        final DoubleList approximations = IntSequence.generate(1, i -> ++i)
                .mapToLong(Generator::fibSum)
                .windowed(2)
                .mapToDouble(w -> (double) w.last() / w.first())
                .takeWhileInclusive(approximation -> !DoubleX.toRoundedString(approximation, scale)
                        .equals(roundedGoldenRatio))
                .onEach(s -> println(DoubleX.toRoundedString(s, scale)))
                .toList();

        final String actual = DoubleX.toRoundedString(approximations.last(), scale);

        assertAll(
                () -> assertEquals(75, approximations.size()),
                () -> assertEquals(roundedGoldenRatio, actual)
        );
    }

    @Test
    void testDistinct() {
        final double[] array = DoubleSequence.of(Math.E, Math.PI, DoubleX.GOLDEN_RATIO, Math.PI, Double.NaN, Double.NaN)
                .distinct()
                .toArray();

        final double[] expected = {Math.E, Math.PI, DoubleX.GOLDEN_RATIO, Double.NaN};

        assertArrayEquals(expected, array);
    }

    @RepeatedTest(value = 2, name = REPEATED_TEST_DISPLAY_NAME)
    void testDoublesToThree() {
        final Triple<Double, DoubleMutableSet, Double> triple = DoubleSequence
                .of(Math.E, Math.PI, DoubleX.GOLDEN_RATIO, Math.PI, Double.NaN, Double.NaN)
                .filterNot(Double::isNaN)
                .doublesToThree(DoubleSequence::sum, DoubleSequence::toMutableSet, DoubleSequence::average);

        assertEquals(10.619501124388526, triple.first());
    }

    @Test
    void testMapIndexed() {
        final var list = DoubleSequence.generate(1, i -> i * DoubleX.GOLDEN_RATIO)
                .mapIndexed((i, d) -> d - i)
                .takeWhile(d -> d < 10)
                .toList();

        assertEquals(DoubleList.of(1.0, 0.6180339887498949, 0.6180339887498949, 1.2360679774997898, 2.8541019662496847, 6.0901699437494745), list);
    }
}
