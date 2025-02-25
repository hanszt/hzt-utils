package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.progressions.IntProgression;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.hzt.utils.collectors.CollectorsX.branching;
import static org.junit.jupiter.api.Assertions.*;

class CollectableTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectableTest.class);

    @Test
    void testTeeingYieldsTwoValuesWhileOnlyGoingThroughPipelineOnce() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8);

        final var toTwoCounter = new Counter();

        final var pair = integers.asSequence()
                .onEach(unused -> toTwoCounter.value++)
                .mapToInt(It::asInt)
                .intsToTwo(IntSequence::sum, IntSequence::average);

        final var collectorCounter = new Counter();

        final var collectedPair = integers.asSequence()
                .onEach(unused -> collectorCounter.value++)
                .teeing(summingLong(It::asInt), averagingInt(It::asInt));

        assertAll(
                () -> assertEquals(pair, collectedPair),
                () -> assertEquals(integers.size(), collectorCounter.value),
                () -> assertEquals(toTwoCounter.value, collectorCounter.value * 2)
        );
    }

    @Test
    void testBranchingYieldsThreeValuesWhileOnlyGoingThroughPipelineOnce() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8);

        final var toThreeCounter = new Counter();

        final var triple = integers.asSequence()
                .onEach(unused -> toThreeCounter.value++)
                .mapToInt(It::asInt)
                .intsToThree(IntSequence::sum, IntSequence::average, IntSequence::stdDev);

        final var branchingCounter = new Counter();

        final var collectedTriple = integers.asSequence()
                .onEach(unused -> branchingCounter.value++)
                .branching(
                        summingLong(It::asInt),
                        averagingInt(It::asInt),
                        CollectorsX.standardDeviatingDouble(It::asInt));

        assertAll(
                () -> assertEquals(triple, collectedTriple),
                () -> assertEquals(integers.size(), branchingCounter.value),
                () -> assertEquals(toThreeCounter.value, branchingCounter.value * 3)
        );
    }

    @Test
    void testBranchSequence() {
        final var leepYearResult = Sequence
                .iterate(LocalDate.of(1950, Month.JANUARY, 1), date -> date.plusDays(1))
                .takeWhileInclusive(date -> date.getYear() <= 2000)
                .filter(LocalDate::isLeapYear)
                .toTwo(Sequence::toList, Sequence::last);

        assertAll(
                () -> assertEquals(4758, leepYearResult.first().size()),
                () -> assertEquals(LocalDate.of(2000, Month.DECEMBER, 31), leepYearResult.second())
        );
    }

    @Test
    void testBranchingPaintingDataToThreeValues() {
        //arrange
        final var paintingList = ListX.of(TestSampleGenerator.createPaintingList());
        final var currentYear = 2024;

        final var expected = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::painter, toList())),
                        summarizingInt(painting -> painting.ageInYears(currentYear)),
                        counting()));

        final var actual = paintingList.asSequence()
                .toThree(s -> s.partitionMapping(Painting::isInMuseum, Painting::painter),
                        s -> s.intStatsOf(painting -> painting.ageInYears(currentYear)),
                        Sequence::count);

        final var expectedStats = expected.second();
        final IntSummaryStatistics actualStats = actual.second();

        assertAll(
                () -> assertEquals(expected.first().get(true), actual.first().first()),
                () -> assertEquals(expectedStats.getAverage(), actualStats.getAverage()),
                () -> assertEquals(expectedStats.getMax(), actualStats.getMax()),
                () -> assertEquals(expected.third().intValue(), actual.third())
        );
    }

    @Test
    void testBranchSequenceToThree() {
        final var triple = IntRange.from(0).until(100)
                .intsToThree(IntSequence::toArray, s -> s.filter(IntX::isEven), IntSequence::stats);

        assertAll(
                () -> assertEquals(100, triple.first().length),
                () -> assertEquals(Year.of(0), triple.second().mapToObj(Year::of).first()),
                () -> assertEquals(49.5, triple.third().getAverage())
        );
    }

    @Test
    void testBranchSequenceToFour() {
        final var actual = IntProgression.from(0).until(100)
                .filter(It::noFilter)
                .boxed()
                .toFour(Sequence::count,
                        s -> s.minOf(It::self),
                        s -> s.maxOf(It::self),
                        s -> s.intSumOf(It::self),
                        IntSummaryStatistics::new);

        assertAll(
                () -> assertEquals(100, actual.getCount()),
                () -> assertEquals(4950, actual.getSum()),
                () -> assertEquals(49.5, actual.getAverage()),
                () -> assertEquals(99, actual.getMax())
        );
    }

    @Test
    void testStreamCanOnlyBeConsumedOnce() {
        final var yearStream = Stream.of(1, 2, 3, 4, 5, 3, -1, 6, 12)
                .filter(IntX::isEven)
                .map(Year::of);

        final var years = yearStream.toList();

        assertAll(
                () -> assertEquals(4, years.size()),
                () -> assertStreamCanOnlyBeConsumedOnce(yearStream)
        );
    }

    @Test
    void testSequenceOfStreamCanOnlyBeConsumedOnce() {
        final var yearStream = Stream.of(1, 2, 3, 4, 5, 3, -1, 6, 12)
                .filter(IntX::isEven)
                .map(Year::of);

        final var yearSequence = Sequence.of(yearStream::iterator);

        assertAll(
                () -> assertEquals(4, yearSequence.count()),
                () -> assertThrows(IllegalStateException.class, yearSequence::toList)
        );
    }

    private void assertStreamCanOnlyBeConsumedOnce(final Stream<Year> yearStream) {
        final var exception = assertThrows(IllegalStateException.class, yearStream::findFirst);
        assertEquals("stream has already been operated upon or closed", exception.getMessage());
    }

    @Test
    void testFrom3DStringArrayToTripleIntArray() {
        final var grid = new String[][][]{{{"1"}, {"2"}, {"3"}}};
        final var expected = Arrays.stream(grid).map(g -> Arrays.stream(g).map(row -> Stream.of(row)
                .mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new)).toArray(int[][][]::new);

        final var actual = Sequence.of(grid).map(g -> Sequence.of(g).map(row -> Sequence.of(row)
                .toIntArray(Integer::parseInt)).toTypedArray(int[][]::new)).toTypedArray(int[][][]::new);

        Sequence.of(actual).map(g -> Sequence.of(g).map(Arrays::toString)).map(Stringable::joinToString).forEach(it -> LOGGER.trace("{}", it));

        assertArrayEquals(expected, actual);
    }

    @Test
    void testBranchToFour() {
        final var sequence = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final var statistics = sequence.branching(
                counting(),
                collectingAndThen(minBy(comparing(It::asInt)), Optional::orElseThrow),
                collectingAndThen(maxBy(comparing(It::asInt)), Optional::orElseThrow),
                summingInt(It::asInt),
                IntSummaryStatistics::new);

        final var stats = sequence.intStatsOf(It::asInt);

        assertAll(
                () -> assertEquals(statistics.getCount(), stats.getCount()),
                () -> assertEquals(statistics.getMin(), stats.getMin()),
                () -> assertEquals(statistics.getMax(), stats.getMax()),
                () -> assertEquals(statistics.getSum(), stats.getSum())
        );
    }

    @Test
    void testToDoubleArray() {
        final var museums = TestSampleGenerator.getMuseumListContainingNulls();
        final var referenceYear = 2022;

        final var averages = Sequence.of(museums)
                .map(Museum::getPaintings)
                .map(Sequence::of)
                .map(s -> s.mapToInt(p -> referenceYear - p.yearOfCreation().getValue()))
                .toDoubleArray(IntSequence::average);

        assertArrayEquals(new double[]{25.0, 135.5, 359.3333333333333, 96.66666666666667}, averages);
    }

    @Test
    void testToTypedArray() {
        final var museums = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museums.stream()
                .map(Museum::getMostPopularPainting)
                .toArray(Painting[]::new);

        final var museumArray = Sequence.of(museums)
                .map(Museum::getMostPopularPainting)
                .toTypedArray(Painting[]::new);

        assertArrayEquals(expected, museumArray);
    }

    @Test
    void testCollectJoining() {
        final var strings = Set.of("collect", "joining", "requires",
                "Collector<? super T, A, R>", "as", "collector", "definition",
                "instead", "of", "Collector<T, A, R>");

        final var expected = String.join(", ", strings);
        final var expected2 = Sequence.of(strings).joinToString();
        final var result = Sequence.of(strings).collect(joining(", "));

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected2, result)
        );
    }

    private static final class Counter {
        int value = 0;
    }
}
