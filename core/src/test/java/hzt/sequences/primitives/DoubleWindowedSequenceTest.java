package hzt.sequences.primitives;

import hzt.collections.ListX;
import hzt.collections.primitives.DoubleListX;
import hzt.sequences.Sequence;
import hzt.utils.It;
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
}
