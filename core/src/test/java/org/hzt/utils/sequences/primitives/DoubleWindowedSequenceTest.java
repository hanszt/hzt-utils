package org.hzt.utils.sequences.primitives;

import hzt.collections.ListX;
import org.hzt.utils.collections.primitives.DoubleListX;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.test.Generator;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleWindowedSequenceTest {

    @Test
    void testPartialWindowedDoubleSequence() {
        double[] array = {1 * Math.PI, 2, 3, 4, 5, 6, 7};

        final double[][] windows = DoubleSequence.of(array)
                .windowed(3, 2, true)
                .map(DoubleListX::toArray)
                .toTypedArray(double[][]::new);

        Sequence.of(windows).map(Arrays::toString).forEach(It::println);

        assertEquals(4, windows.length);
    }

    @Test
    void testLargeVariableWindowedSequence() {
        final ListX<DoubleListX> windows = DoubleSequence.generate(0, pi -> pi + Math.PI)
                .take(2_000_000)
                .windowed(2000, size -> --size, 1, step -> ++step)
                .onEach(w -> It.println(w.size()))
                .toListX();

        final DoubleListX lastWindow = windows.last();

        final DoubleListX firstWindow = windows.first();

        final ListX<DoubleListX> head = windows.headTo(3);

        head.forEach(It::println);

        final ListX<DoubleListX> tail = windows.tailFrom(windows.size() - 3);

        tail.forEach(It::println);

        assertAll(
                () -> assertEquals(2000, windows.size()),
                () -> assertEquals(2000, firstWindow.size()),
                () -> assertEquals(1, lastWindow.size()),
                () -> assertEquals(6280043.714399726, lastWindow.single())
        );
    }

    @Test
    void testApproximateGoldenRatioUsingDoubleSequence() {
        double goldenRatio = (1 + Math.sqrt(5)) / 2;
        final int scale = 20;

        final DoubleListX approximations = IntSequence.generate(1, i -> ++i)
                .mapToLong(Generator::fibSum)
                .windowed(2)
                .mapToDouble(w -> (double) w.last() / w.first())
                .takeWhileInclusive(gr -> !DoubleX.toRoundedString(gr, scale)
                        .equals(DoubleX.toRoundedString(goldenRatio, scale)))
                .toListX();

        final String expected = DoubleX.toRoundedString(goldenRatio, scale);
        final String actual = DoubleX.toRoundedString(approximations.last(), scale);

        assertAll(
                () -> assertEquals(75, approximations.size()),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testVariableSizedChunkedDoubleSequence() {
        final ListX<DoubleListX> chunks = DoubleSequence.generate(0, i -> i + Math.E)
                .chunked(1, Generator::sawTooth)
                .take(100)
                .toListX();

        chunks.forEach(It::println);

        assertEquals(5, chunks.count(chunk -> chunk.size() == 1));
    }
}
