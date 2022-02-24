package hzt.sequences;

import hzt.collections.ListX;
import hzt.ranges.IntRange;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WindowedSequenceTest {

    @Test
    void testChunkedSequence() {
        final List<Pair<Long, Long>> sumDays = Sequence
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
        final List<Pair<Long, Long>> sumDays = Sequence
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
        final var chunkSizes = Sequence.generate(0, i -> ++i)
                .chunked(sineWaveGenerator())
                .take(20)
                .onEach(It::println)
                .mapToLong(ListX::size)
                .toArray();

        assertArrayEquals(new long[] {1, 3, 5, 8, 10, 12, 14, 15, 15, 15, 14, 13, 11, 9, 6, 4, 2, 1, 1, 1}, chunkSizes);
    }

    @NotNull
    private IntSupplier sineWaveGenerator() {
        return new IntSupplier() {
            int x = -3;

            @Override
            public int getAsInt() {
                return (int) (8 * (1 + Math.sin(.3 * x++)));
            }
        };
    }

    @Test
    void testVariableStepAndSizeWindowedSequence() {
        final var windows = IntRange.closed(4, 50).boxed()
                .windowed(5, size -> ++size, 10, step -> --step, true)
                .toListX();

        System.out.println("windows = " + windows);

        assertAll(
                () -> assertEquals(ListX.of(4, 5, 6, 7, 8), windows.first()),
                () -> assertEquals(ListX.of(49, 50), windows.last())
        );
    }
}
