package org.hzt.utils.sequences.primitives;

import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleWindowedSequenceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoubleWindowedSequenceTest.class);

    @Test
    void testPartialWindowedDoubleSequence() {
        final var array = new double[]{1 * Math.PI, 2, 3, 4, 5, 6, 7};

        final var windows = DoubleSequence.of(array)
                .windowed(3, 2, true)
                .map(DoubleList::toArray)
                .toTypedArray(double[][]::new);

        Sequence.of(windows).map(Arrays::toString).forEach(w -> LOGGER.debug("window: {}", w));

        assertThat(windows).hasNumberOfRows(4);
    }

    @Test
    void testLargeVariableWindowedSequence() {
        final var windows = DoubleSequence.iterate(0, pi -> pi + Math.PI)
                .take(2_000_000)
                .windowed(2000, size -> --size, 1, step -> ++step)
                .onEach(w -> LOGGER.atDebug().setMessage(() -> "size: " + w.size()).log())
                .toListX();

        final var lastWindow = windows.last();

        final var firstWindow = windows.first();

        final var head = windows.headTo(3);

        head.forEach(w -> LOGGER.debug("window: {}", w));

        assertAll(
                () -> assertEquals(2000, windows.size()),
                () -> assertEquals(2000, firstWindow.size()),
                () -> assertEquals(1, lastWindow.size()),
                () -> assertEquals(6280043.714399726, lastWindow.single())
        );
    }

    @Test
    void testApproximateGoldenRatioUsingDoubleSequence() {
        final var goldenRatio = (1 + Math.sqrt(5)) / 2;
        final var scale = 20;

        final var approximations = IntSequence.iterate(1, i -> ++i)
                .mapToLong(Generator::fibSum)
                .windowed(2)
                .mapToDouble(w -> (double) w.last() / w.first())
                .takeWhileInclusive(gr -> !DoubleX.toRoundedString(gr, scale)
                        .equals(DoubleX.toRoundedString(goldenRatio, scale)))
                .toList();

        final var expected = DoubleX.toRoundedString(goldenRatio, scale);
        final var actual = DoubleX.toRoundedString(approximations.last(), scale);

        assertAll(
                () -> assertEquals(75, approximations.size()),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testVariableSizedChunkedDoubleSequence() {
        final var chunks = DoubleSequence.iterate(0, i -> i + Math.E)
                .chunked(1, Generator::sawTooth)
                .take(100)
                .toListX();

        chunks.forEach(it -> LOGGER.debug("chunk: {}", it));

        assertEquals(5, chunks.count(chunk -> chunk.size() == 1));
    }
}
