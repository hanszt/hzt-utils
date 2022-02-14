package hzt.sequences;

import hzt.tuples.Pair;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

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
                .chunked(12, dates -> dates.sumOfInts(LocalDate::getDayOfYear))
                .zipWithNext(Pair::of)
                .takeWhileInclusive(p -> p.first().equals(p.second()))
                .toList();

        sumDays.forEach(It::println);

        assertEquals(4, sumDays.size());
    }
}
