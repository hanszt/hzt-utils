package hzt.sequences;

import hzt.collections.ListX;
import hzt.collections.primitives.LongListX;
import hzt.iterables.Numerable;
import hzt.ranges.IntRange;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WindowedSequenceTest {

    @Test
    void testChunkedSequence() {
        final var sumDays = Sequence
                .generate(LocalDate.of(1900, Month.JANUARY, 1), date -> date.plusMonths(1))
                .chunked(12)
                .map(dates -> dates.sumOfInts(LocalDate::getDayOfYear))
                .zipWithNext(Pair::of)
                .takeWhileInclusive(p -> p.first().equals(p.second()))
                .toList();

        sumDays.forEach(It::println);

        assertEquals(4, sumDays.size());
    }

    @Test
    void testChunkedMappingSequence() {
        final var sumDays = Sequence
                .generate(LocalDate.of(1900, Month.JANUARY, 1), date -> date.plusMonths(1))
                .chunked(12)
                .map(dates -> dates.sumOfInts(LocalDate::getDayOfYear))
                .zipWithNext(Pair::of)
                .takeWhileInclusive(p -> p.first().equals(p.second()))
                .toList();

        sumDays.forEach(It::println);

        assertEquals(4, sumDays.size());
    }

    @Test
    void testVariableSizeChunkedSequenceSineShape() {
        var x = new AtomicInteger(-3);

        final var chunkedCounts = Sequence
                .generate(0, value -> ++value)
                .chunked(1, size -> (int) (8 * (1 + Math.sin(.3 * x.getAndIncrement()))))
                .take(20)
                .onEach(It::println)
                .mapToLong(Numerable::count)
                .toListX();

        assertEquals(LongListX.of(1, 1, 3, 5, 8, 10, 12, 14, 15, 15, 15, 14, 13, 11, 9, 6, 4, 2, 1, 1), chunkedCounts);
    }

    private int getAnInt(AtomicInteger nextNr) {
        final var val = (int) (8 * (1 + Math.sin(.3 * nextNr.getAndIncrement())));
        System.out.println("val = " + val);
        return val;
    }

    @Test
    void testVariableStepAndSizeWindowedSequence() {
        final var windows = IntRange.closed(4, 50).boxed()
                .windowed(5, nextSize -> ++nextSize, 10, nextStep -> --nextStep, true)
                .toListX();

        System.out.println("windows = " + windows);

        assertAll(
                () -> assertEquals(ListX.of(4, 5, 6, 7, 8), windows.first()),
                () -> assertEquals(ListX.of(49, 50), windows.last())
        );
    }
}
