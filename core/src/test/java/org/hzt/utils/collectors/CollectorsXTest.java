package org.hzt.utils.collectors;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.statistics.BigDecimalStatistics;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Gatherers.fold;
import static org.hzt.utils.collectors.BigDecimalCollectors.summarizingBigDecimal;
import static org.hzt.utils.collectors.CollectorsX.*;
import static org.hzt.utils.gatherers.GatherersX.*;
import static org.junit.jupiter.api.Assertions.*;

class CollectorsXTest {

    @Test
    void testBranchingToBigDecimalSummaryStatistics() {
        final var sampleBankAccountListContainingNulls = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final var expected = sampleBankAccountListContainingNulls.stream()
                .filter(Objects::nonNull)
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var actual = Sequence.of(sampleBankAccountListContainingNulls).toFour(
                Sequence::count,
                s -> s.bigDecimalSumOf(BankAccount::getBalance),
                s -> s.minOf(BankAccount::getBalance),
                s -> s.maxOf(BankAccount::getBalance),
                BigDecimalStatistics::new
        );

        assertAll(
                () -> assertNotEquals(expected.getAverage(), actual.getAverage()),
                () -> assertNotEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getMin(), actual.getMin()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void testStandardDeviatingAgePainters() {
        final var paintingList = TestSampleGenerator.createPaintingList();
        final var currentYear = 2024;

        final double standardDeviationAge = paintingList.stream()
                .collect(standardDeviatingDouble(painting -> painting.ageInYears(currentYear)));

        final var summarizingAges = paintingList.stream()
                .collect(toDoubleStatisticsBy(painting -> painting.ageInYears(currentYear)));

        final var optionalAverage = paintingList.stream()
                .mapToDouble(painting -> painting.ageInYears(currentYear))
                .average();

        assertAll(
                () -> assertEquals(206.875, summarizingAges.getAverage(), 1e-3),
                () -> assertEquals(120.875, summarizingAges.getStandardDeviation(), 1e-3),
                () -> assertEquals(standardDeviationAge, summarizingAges.getStandardDeviation()),
                () -> assertEquals(optionalAverage.orElseThrow(), summarizingAges.getAverage())
        );
    }

    @Test
    void testToIntersection() {
        final var list1 = List.of("Hoi", "hoe", "het", "met", "jou", "lol");
        final var list2 = List.of("Dit", "is", "een", "zin", "Hoi", "Papa", "lol");
        final var list3 = List.of("Lalalala", "Nog meer", "zinnen", "Hoi", "Lief", "lol");
        final var list4 = List.of("Hoi", "rere", "lol", "serse", "aweaw");
        final var list5 = List.of("lol", "asdad", "wer", "werwe", "Hoi");
        final var list6 = List.of("sdfsf", "", "awr", "awr", "Hoi", "lol");

        final var stringLists = List.of(list1, list2, list3, list4, list5, list6);

        final var intersection = stringLists.stream()
                .collect(toIntersection());

        It.println("intersection = " + intersection);

        assertAll(
                () -> assertEquals(2, intersection.size()),
                () -> assertTrue(intersection.containsAll(List.of("Hoi", "lol"))),
                () -> assertEquals(new HashSet<>(List.of("Hoi", "lol")), intersection)
        );
    }

    @Test
    void testIntersectingBy() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var paintingNamesPresentInAllMuseums = museumList.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::getMillenniumOfCreation));

        It.println("paintingMadeInPreviousMilleniumPresentInAllMuseums = " + paintingNamesPresentInAllMuseums);

        assertFalse(paintingNamesPresentInAllMuseums.isEmpty());
    }

    @Nested
    class GathererAsCollectorTests {

        @Test
        void testMapFilterReduce() {
            final var mapFilterFold = map((LocalDate date) -> YearMonth.of(date.getYear(), date.getMonth()))
                    .andThen(filter(YearMonth::isLeapYear))
                    .andThen(fold(() -> 0, (acc, ym) -> acc + ym.getYear() + ym.getMonthValue()));

            var dates = Stream.iterate(LocalDate.of(2023, 10, 1), date -> date.plus(Period.of(3, 2, 1)))
                    .limit(100)
                    .toList();

            var list = dates.stream()
                    .gather(mapFilterFold)
                    .toList();

            final var byCollector = dates.stream().collect(toList(mapFilterFold));

            assertEquals(1, list.size());
            assertEquals(54_870, list.getFirst());
            assertEquals(list, byCollector);
        }

        @Test
        void testShortCircuitingNotSupportedInCollectors() {
            final var gatherer = map((LocalDate d) -> YearMonth.of(d.getYear(), d.getMonth()))
                    .andThen(filter(YearMonth::isLeapYear))
                    .andThen(limit(10));

            var list = Stream.iterate(LocalDate.of(2023, 10, 1), date -> date.plus(Period.of(3, 2, 1)))
                    .gather(gatherer)
                    .toList();

            final var collector = toList(gatherer);

            var stream = Stream.iterate(LocalDate.of(2023, 10, 1), date -> date.plus(Period.of(3, 2, 1)));

            assertThrows(IllegalStateException.class, () -> stream.collect(collector));
            assertEquals(10, list.size());
        }
    }
}
