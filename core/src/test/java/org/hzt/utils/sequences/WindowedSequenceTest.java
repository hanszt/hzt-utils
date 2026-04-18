package org.hzt.utils.sequences;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.IntNumerable;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.IntSupplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WindowedSequenceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(WindowedSequenceTest.class);

    @Test
    void testChunkedSequence() {
        final var sumDays = Sequence
                .iterate(LocalDate.of(1900, Month.JANUARY, 1), date -> date.plusMonths(1))
                .chunked(12)
                .map(dates -> dates.intSumOf(LocalDate::getDayOfYear))
                .zipWithNext(Pair::of)
                .takeWhileInclusive(p -> p.first().equals(p.second()))
                .toList();

        sumDays.forEach(it -> LOGGER.trace("{}", it));

        assertEquals(4, sumDays.size());
    }

    @Test
    void testChunkedMappingSequence() {
        final var sumDays = Sequence
                .iterate(LocalDate.of(1900, Month.JANUARY, 1), date -> date.plusMonths(1))
                .chunked(12)
                .map(dates -> dates.mapToInt(LocalDate::getDayOfYear))
                .onEach(days -> days.forEachInt(it -> LOGGER.trace("{}", it)))
                .map(IntNumerable::sum)
                .zipWithNext(Pair::of)
                .takeWhileInclusive(p -> p.first().equals(p.second()))
                .toList();

        sumDays.forEach(it -> LOGGER.trace("{}", it));

        assertEquals(4, sumDays.size());
    }

    @Test
    void testVariableSizeChunkedSequenceSineShape() {
        final var chunkSizes = Sequence.iterate(0, i -> i + 2)
                .chunked(sineWaveGenerator())
                .take(20)
                .onEach(it -> LOGGER.trace("{}", it))
                .mapToLong(ListX::size)
                .toArray();

        assertArrayEquals(new long[]{1, 3, 5, 8, 10, 12, 14, 15, 15, 15, 14, 13, 11, 9, 6, 4, 2, 1, 1, 1}, chunkSizes);
    }

    private IntSupplier sineWaveGenerator() {
        return new IntSupplier() {
            int x = -3;

            @Override
            public int getAsInt() {
                return (int) (8 * (1 + Math.sin(0.3 * x++)));
            }
        };
    }

    @Test
    void testVariableStepAndSizeWindowedSequence() {
        final var windows = IntRange.closed(4, 50).boxed()
                .windowed(5, size -> ++size, 10, step -> --step, true)
                .onSequence(w -> w
                        .zipWithNext()
                        .forEach((w1, w2) -> LOGGER.atDebug().setMessage(() -> "dif: " + (w2.first() - w1.first())).log()))
                .toListX();

        LOGGER.atDebug().setMessage(() -> "windows = " + windows).log();

        assertAll(
                () -> assertEquals(ListX.of(4, 5, 6, 7, 8), windows.first()),
                () -> assertEquals(ListX.of(49, 50), windows.last())
        );
    }

    @Nested
    class NextChunkIf {

        @Test
        void testNextChunkIf() {
            final var chunks = Sequence.of(1, 2, 3, 4, 5, 6, 2, 4, 5, 123, 2, 3, 1)
                    .nextChunkedIf(i -> i == 3)
                    .toList();

            assertThat(chunks).containsExactly(
                    ListX.of(1, 2),
                    ListX.of(3, 4, 5, 6, 2, 4, 5, 123, 2),
                    ListX.of(3, 1)
            );
        }

        @Test
        void testNextChunkIfStartWithPredicate() {
            final var chunks = Sequence.of(3, 1, 2, 3, 4, 5, 6, 2, 4, 5, 123, 2, 3)
                    .nextChunkedIf(i -> i == 3)
                    .toList();

            assertThat(chunks).containsExactly(
                    ListX.of(3, 1, 2),
                    ListX.of(3, 4, 5, 6, 2, 4, 5, 123, 2),
                    ListX.of(3)
            );
        }

    }
}
