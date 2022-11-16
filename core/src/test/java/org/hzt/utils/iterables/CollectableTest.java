package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.progressions.IntProgression;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.tuples.Triple;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.hzt.utils.collectors.CollectorsX.branching;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CollectableTest {

    @Test
    void testTeeingYieldsTwoValuesWhileOnlyGoingThroughPipelineOnce() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8);

        final AtomicInteger toTwoCounter = new AtomicInteger();

        final Pair<Long, Double> pair = integers.asSequence()
                .onEach(i -> toTwoCounter.incrementAndGet())
                .mapToInt(It::asInt)
                .intsToTwo(IntSequence::sum, IntSequence::average);

        final AtomicInteger collectorCounter = new AtomicInteger();

        final Pair<Long, Double> collectedPair = integers.asSequence()
                .onEach(i -> collectorCounter.incrementAndGet())
                .teeing(summingLong(It::asInt), averagingInt(It::asInt));

        assertAll(
                () -> assertEquals(pair, collectedPair),
                () -> assertEquals(integers.size(), collectorCounter.get()),
                () -> assertEquals(toTwoCounter.get(), collectorCounter.get() * 2)
        );
    }

    @Test
    void testBranchingYieldsThreeValuesWhileOnlyGoingThroughPipelineOnce() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8);

        final AtomicInteger toThreeCounter = new AtomicInteger();

        final Triple<Long, Double, Double> triple = integers.asSequence()
                .onEach(i -> toThreeCounter.incrementAndGet())
                .mapToInt(It::asInt)
                .intsToThree(IntSequence::sum, IntSequence::average, IntSequence::stdDev);

        final AtomicInteger branchingCounter = new AtomicInteger();

        final Triple<Long, Double, Double> collectedTriple = integers.asSequence()
                .onEach(i -> branchingCounter.incrementAndGet())
                .branching(
                        summingLong(It::asInt),
                        averagingInt(It::asInt),
                        CollectorsX.standardDeviatingDouble(It::asInt));

        assertAll(
                () -> assertEquals(triple, collectedTriple),
                () -> assertEquals(integers.size(), branchingCounter.get()),
                () -> assertEquals(toThreeCounter.get(), branchingCounter.get() * 3)
        );
    }

    @Test
    void testBranchSequence() {
        final Pair<List<LocalDate>, LocalDate> leepYearResult = Sequence
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
        final ListX<Painting> paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final Triple<Map<Boolean, List<Painter>>, IntSummaryStatistics, Long> expected = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::painter, toList())),
                        summarizingInt(Painting::ageInYears),
                        counting()));

        final Triple<Pair<ListX<Painter>,ListX<Painter>>, IntStatistics, Long> actual = paintingList.asSequence()
                .toThree(s -> s.partitionMapping(Painting::isInMuseum, Painting::painter),
                        s -> s.intStatsOf(Painting::ageInYears),
                        Sequence::count);

        final IntSummaryStatistics expectedStats = expected.second();
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
        final Triple<int[], IntSequence, IntStatistics> triple = IntRange.from(0).until(100)
                .intsToThree(IntSequence::toArray, s -> s.filter(IntX::isEven), IntSequence::stats);

        assertAll(
                () -> assertEquals(100, triple.first().length),
                () -> assertEquals(Year.of(0), triple.second().mapToObj(Year::of).first()),
                () -> assertEquals(49.5, triple.third().getAverage())
        );
    }

    @Test
    void testBranchSequenceToFour() {
        final ListX<? extends Number> actual = IntProgression.from(0).until(100)
                .filter(It::noFilter)
                .boxed()
                .toFour(Sequence::count,
                        s -> s.minOf(It::self),
                        s -> s.maxOf(It::self),
                        s -> s.intSumOf(It::self),
                        ListX::of);

        assertAll(
                () -> assertEquals(100, actual.first().longValue()),
                () -> assertEquals(0, actual.get(1).intValue()),
                () -> assertEquals(99, actual.get(2).intValue()),
                () -> assertEquals(4950, actual.last().longValue())
        );
    }

    @Test
    void testStreamCanOnlyBeConsumedOnce() {
        final Stream<Year> yearStream = Stream.of(1, 2, 3, 4, 5, 3, -1, 6, 12)
                .filter(IntX::isEven)
                .map(Year::of);

        final List<Year> years = yearStream.collect(toList());

        assertAll(
                () -> assertEquals(4, years.size()),
                () -> assertStreamCanOnlyBeConsumedOnce(yearStream)
        );
    }

    @Test
    void testSequenceOfStreamCanOnlyBeConsumedOnce() {
        final Stream<Year> yearStream = Stream.of(1, 2, 3, 4, 5, 3, -1, 6, 12)
                .filter(IntX::isEven)
                .map(Year::of);

        final Sequence<Year> yearSequence = Sequence.of(yearStream::iterator);

        assertAll(
                () -> assertEquals(4, yearSequence.count()),
                () -> assertThrows(IllegalStateException.class, yearSequence::toList)
        );
    }

    private void assertStreamCanOnlyBeConsumedOnce(Stream<Year> yearStream) {
        //noinspection ResultOfMethodCallIgnored
        final IllegalStateException exception = assertThrows(IllegalStateException.class, yearStream::findFirst);
        assertEquals("stream has already been operated upon or closed", exception.getMessage());
    }

    @Test
    void testFrom3DStringArrayToTripleIntArray() {
        String[][][] grid = {{{"1"}, {"2"}, {"3"}}};
        final int[][][] expected = Arrays.stream(grid).map(g -> Arrays.stream(g).map(row -> Stream.of(row)
                .mapToInt(Integer::parseInt).toArray()).toArray(int[][]::new)).toArray(int[][][]::new);

        final int[][][] actual = Sequence.of(grid).map(g -> Sequence.of(g).map(row -> Sequence.of(row)
                .toIntArray(Integer::parseInt)).toTypedArray(int[][]::new)).toTypedArray(int[][][]::new);

        Sequence.of(actual).map(g -> Sequence.of(g).map(Arrays::toString)).map(Stringable::joinToString).forEach(It::println);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testBranchToFour() {
        final Sequence<Integer> sequence = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final ListX<? extends Number> statistics = sequence.branching(
                counting(),
                collectingAndThen(minBy(comparing(It::asInt)), integer -> integer.orElseThrow(NoSuchElementException::new)),
                collectingAndThen(maxBy(comparing(It::asInt)), integer1 -> integer1.orElseThrow(NoSuchElementException::new)),
                summingInt(It::asInt),
                ListX::of);

        final IntStatistics stats = sequence.intStatsOf(It::asInt);

        assertAll(
                () -> assertEquals(statistics.first().longValue(), stats.getCount()),
                () -> assertEquals(statistics.get(1).intValue(), stats.getMin()),
                () -> assertEquals(statistics.get(2).intValue(), stats.getMax()),
                () -> assertEquals(statistics.last().longValue(), stats.getSum())
        );
    }

    @Test
    void testToDoubleArray() {
        final List<Museum> museums = TestSampleGenerator.getMuseumListContainingNulls();

        final double[] averages = Sequence.of(museums)
                .map(Museum::getPaintings)
                .map(Sequence::of)
                .map(s -> s.mapToInt(Painting::ageInYears))
                .toDoubleArray(IntSequence::average);

        assertArrayEquals(new double[]{25.0, 135.5, 359.3333333333333, 96.66666666666667}, averages);
    }

    @Test
    void testToTypedArray() {
        final List<Museum> museums = TestSampleGenerator.getMuseumListContainingNulls();

        final Painting[] expected = museums.stream()
                .map(Museum::getMostPopularPainting)
                .toArray(Painting[]::new);

        final Painting[] museumArray = Sequence.of(museums)
                .map(Museum::getMostPopularPainting)
                .toTypedArray(Painting[]::new);

        assertArrayEquals(expected, museumArray);
    }

    @Test
    void testCollectJoining() {
        Set<String> strings = new HashSet<>(Arrays.asList("collect", "joining", "requires",
                "Collector<? super T, A, R>", "as", "collector", "definition",
                "instead", "of", "Collector<T, A, R>"));

        final String expected = String.join(", ", strings);
        final String expected2 = Sequence.of(strings).joinToString();
        final String result = Sequence.of(strings).collect(joining(", "));

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected2, result)
        );
    }
}
