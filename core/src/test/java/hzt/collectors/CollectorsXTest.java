package hzt.collectors;

import hzt.sequences.Sequence;
import hzt.statistics.BigDecimalStatistics;
import hzt.statistics.BigDecimalSummaryStatistics;
import hzt.statistics.DoubleStatistics;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;

import static hzt.collectors.BigDecimalCollectors.summarizingBigDecimal;
import static hzt.collectors.CollectorsX.intersectingBy;
import static hzt.collectors.CollectorsX.toIntersection;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectorsXTest {

    @Test
    void testBranchingToBigDecimalSummaryStatistics() {
        final List<BankAccount> sampleBankAccountListContainingNulls = TestSampleGenerator.createSampleBankAccountListContainingNulls();

        final BigDecimalSummaryStatistics expected = sampleBankAccountListContainingNulls.stream()
                .filter(Objects::nonNull)
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var actual = Sequence.of(sampleBankAccountListContainingNulls).toFour(
                Sequence::count,
                s -> s.bigDecimalSum(BankAccount::getBalance),
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
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final double standardDeviationAge = paintingList.stream()
                .collect(CollectorsX.standardDeviatingDouble(Painting::ageInYears));

        final DoubleStatistics summarizingAges = paintingList.stream()
                .collect(CollectorsX.toDoubleStatisticsBy(Painting::ageInYears));

        OptionalDouble optionalAverage = paintingList.stream()
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
        final List<String> list1 = Arrays.asList("Hoi", "hoe", "het", "met", "jou", "lol");
        final List<String> list2 = Arrays.asList("Dit", "is", "een", "zin", "Hoi", "Papa", "lol");
        final List<String> list3 = Arrays.asList("Lalalala", "Nog meer", "zinnen", "Hoi", "Lief", "lol");
        final List<String> list4 = Arrays.asList("Hoi", "rere", "lol", "serse", "aweaw");
        final List<String> list5 = Arrays.asList("lol", "asdad", "wer", "werwe", "Hoi");
        final List<String> list6 = Arrays.asList("sdfsf", "", "awr", "awr", "Hoi", "lol");

        List<List<String>> stringLists = Arrays.asList(list1, list2, list3, list4, list5, list6);

        final Set<String> intersection = stringLists.stream()
                .collect(toIntersection());

        It.println("intersection = " + intersection);

        assertAll(
                () -> assertEquals(2, intersection.size()),
                () -> assertTrue(intersection.containsAll(Arrays.asList("Hoi", "lol"))),
                () -> assertEquals(new HashSet<>(Arrays.asList("Hoi", "lol")), intersection)
        );
    }

    @Test
    void testIntersectingBy() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        Set<Period> paintingNamesPresentInAllMuseums = museumList.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::getMilleniumOfCreation));

        It.println("paintingMadeInPreviousMilleniumPresentInAllMuseums = " + paintingNamesPresentInAllMuseums);

        assertFalse(paintingNamesPresentInAllMuseums.isEmpty());
    }
}
