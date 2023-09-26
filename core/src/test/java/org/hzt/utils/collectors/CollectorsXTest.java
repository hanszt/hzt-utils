package org.hzt.utils.collectors;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.statistics.BigDecimalStatistics;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.hzt.utils.collectors.BigDecimalCollectors.summarizingBigDecimal;
import static org.hzt.utils.collectors.CollectorsX.intersectingBy;
import static org.hzt.utils.collectors.CollectorsX.toIntersection;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        final double standardDeviationAge = paintingList.stream()
                .collect(CollectorsX.standardDeviatingDouble(Painting::ageInYears));

        final var summarizingAges = paintingList.stream()
                .collect(CollectorsX.toDoubleStatisticsBy(Painting::ageInYears));

        final var optionalAverage = paintingList.stream()
                .mapToDouble(Painting::ageInYears)
                .average();

        It.println("summarizingAges = " + summarizingAges);

        assertAll(
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
}
