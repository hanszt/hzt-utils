package org.hzt.utils.gatherers;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.statistics.DoubleStatistics;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.statistics.LongStatistics;
import org.hzt.utils.streams.StreamX;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hzt.utils.collectors.CollectorsX.doubleArrayOf;
import static org.hzt.utils.collectors.CollectorsX.intArrayOf;
import static org.hzt.utils.collectors.CollectorsX.longArrayOf;
import static org.hzt.utils.gatherers.GatherersX.chunked;
import static org.hzt.utils.gatherers.GatherersX.distinctBy;
import static org.hzt.utils.gatherers.GatherersX.dropWhile;
import static org.hzt.utils.gatherers.GatherersX.mapNotNull;
import static org.hzt.utils.gatherers.GatherersX.runningDoubleStatisticsOf;
import static org.hzt.utils.gatherers.GatherersX.runningIntStatisticsOf;
import static org.hzt.utils.gatherers.GatherersX.runningLongStatisticsOf;
import static org.hzt.utils.gatherers.GatherersX.sorted;
import static org.hzt.utils.gatherers.GatherersX.sortedBy;
import static org.hzt.utils.gatherers.GatherersX.sortedDescendingBy;
import static org.hzt.utils.gatherers.GatherersX.sortedDistinct;
import static org.hzt.utils.gatherers.GatherersX.takeWhile;
import static org.hzt.utils.gatherers.GatherersX.takeWhileIncluding;
import static org.hzt.utils.gatherers.GatherersX.windowed;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GatherersXTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatherersXTest.class);

    @Test
    void testDistinctBy() {
        final var organizations = Sequence.of("KLM", "klm", "NS", "asml", "van gogh", "ASML", "ns")
                .map(Organization::new)
                .gather(distinctBy(s -> s.name.toLowerCase()))
                .toList();

        final var expectedOrgs = Sequence.of("KLM", "NS", "asml", "van gogh")
                .map(Organization::new)
                .toList();

        assertEquals(expectedOrgs, organizations);
    }

    private record Organization(String name) {

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final var that = (Organization) o;
            return Objects.equals(name, that.name);
        }

    }

    @Test
    void testMapNotNull() {
        final var courseRepo = MapX.of(
                "A", "Math",
                "F", "Science",
                "R", "History",
                "L", "Politics"
        );

        final var courseIds = new String[]{"A", null, "F", null, "R", "P", "Z", "T"};

        final var result = Sequence.of(courseIds)
                .gather(mapNotNull(courseRepo::get))
                .toListX();

        final var byMapMulti = Sequence.of(courseIds)
                .mapMulti(notNull(courseRepo::get))
                .toListX();

        assertAll(
                () -> assertEquals(ListX.of("Math", "Science", "History"), result),
                () -> assertEquals(byMapMulti, result)
        );
    }

    @Test
    void testDropWhile() {
        final var result = StreamX.iterate(1, i1 -> i1 + 1)
                .limit(10)
                .gather(dropWhile(i -> i != 5))
                .toList();

        final var takeWhileResult = StreamX.iterate(1, i1 -> i1 + 1)
                .limit(10)
                .dropWhile(i -> i != 5)
                .toList();

        final var expected = List.of(5, 6, 7, 8, 9, 10);

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected, takeWhileResult)
        );
    }

    @Test
    void testTakeWhile() {
        final var integers = Sequence.iterate(1, i -> i + 1);

        final var result = integers
                .gather(takeWhile(i -> i != 5))
                .toList();

        final var takeWhileResult = integers
                .takeWhile(i -> i != 5)
                .toList();

        final var expected = List.of(1, 2, 3, 4);

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected, takeWhileResult)
        );
    }

    @Test
    void testTakeWhileInclusive() {
        final Predicate<Integer> isOdd = i -> i % 2 != 0;
        final var integers = Sequence.of(1, 3, 5, 6, 7, 8, 10, 12);

        final var result = integers
                .gather(takeWhileIncluding(isOdd))
                .toList();

        final var takeWhileResult = integers
                .takeWhile(isOdd)
                .toList();

        assertAll(
                () -> assertEquals(List.of(1, 3, 5, 6), result),
                () -> assertEquals(List.of(1, 3, 5), takeWhileResult)
        );
    }

    private static <T, R> BiConsumer<? super T, Consumer<R>> notNull(final Function<? super T, ? extends R> mapper) {
        return (item, consumer) -> GatherersX.acceptIfResultNotNull(mapper, item, consumer);
    }

    @Nested
    class SortedTests {

        @Test
        void testSortedBySelector() {

            final var list = ListX.of(5, 6, 8, 4, 12, 15, 16, 4);

            final var result = Sequence.of(list)
                    .map(Person::new)
                    .gather(sortedBy(Person::age))
                    .toList();

            final var expected = Sequence.of(list)
                    .map(Person::new)
                    .sorted(comparing(Person::age))
                    .toList();

            assertEquals(expected, result);
        }

        private record Person(int age) {

            @Override
            public boolean equals(final Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                final var person = (Person) o;
                return age == person.age;
            }

        }

        @Test
        void testSortedDescendingBySelector() {
            final var list = ListX.of(5, 6, 8, 4, 12, 15, 16, 4);

            final var result = list.stream()
                    .map(Person::new)
                    .gather(sortedDescendingBy(Person::age))
                    .toList();

            final var bySortedMethod = list.stream()
                    .map(Person::new)
                    .sorted(comparing(Person::age).reversed())
                    .toList();

            final var expected = StreamX.of(16, 15, 12, 8, 6, 5, 4, 4).map(Person::new).toList();

            assertAll(
                    () -> assertEquals(expected, result),
                    () -> assertEquals(bySortedMethod, result)
            );
        }

        @Test
        void testSorted() {
            final var list = ListX.of("A", "B", "A", "F", "F", "R", "P", "Z", "T");

            final var result = list.stream()
                    .gather(sorted())
                    .toList();

            final var expected = list.stream()
                    .sorted()
                    .toList();

            assertEquals(expected, result);
        }

        @Test
        void testSortedDistinct() {
            final var list = ListX.of("A", "B", "A", "F", "F", "R", "P", "Z", "T");

            final var result = list.stream()
                    .gather(sortedDistinct())
                    .toList();

            final var resultBySequence = Sequence.of(list)
                    .gather(sortedDistinct())
                    .toList();

            final var expected = list.stream()
                    .distinct()
                    .sorted()
                    .toList();

            assertAll(
                    () -> assertEquals(expected, result),
                    () -> assertEquals(expected, resultBySequence)
            );
        }
    }


    @Nested
    class SummaryStatisticsTests {

        @Test
        void testRunningIntStatistics() {
            final var integers = IntSequence.of(5, 6, 8, 4, 12, 15, 16, 4);

            final var result = integers
                    .mapToObj(Person::new)
                    .gather(runningIntStatisticsOf(Person::age))
                    .teeing(intArrayOf(IntStatistics::getMax), longArrayOf(IntStatistics::getSum));

            final var sums = integers
                    .boxed()
                    .mapMulti(runningStatistics())
                    .mapToLong(IntStatistics::getSum)
                    .toArray();

            final var expectedMaxes = new int[]{5, 6, 8, 8, 12, 15, 16, 16};
            final long[] expectedSums = {5, 11, 19, 23, 35, 50, 66, 70};

            assertAll(
                    () -> assertArrayEquals(expectedMaxes, result.first()),
                    () -> assertArrayEquals(expectedSums, result.second()),
                    () -> assertArrayEquals(expectedSums, sums)
            );
        }

        private BiConsumer<Integer, Consumer<IntStatistics>> runningStatistics() {
            final var stats = new IntStatistics();
            return (value, consumer) -> {
                stats.accept(value);
                consumer.accept(new IntStatistics().combine(stats));
            };
        }

        private record Person(int age) {
        }

        @Test
        void testRunningLongStatistics() {
            final var result = LongSequence.of(5, 6, 8, 4, 12, 15, Long.MAX_VALUE, 4)
                    .mapToObj(ChemicalSubstance::new)
                    .gather(runningLongStatisticsOf(ChemicalSubstance::mol))
                    .onEach(System.out::println)
                    .teeing(longArrayOf(LongStatistics::getMax), longArrayOf(LongStatistics::getSum));

            final long[] expectedMaxes = {5, 6, 8, 8, 12, 15, Long.MAX_VALUE, Long.MAX_VALUE};
            final long[] expectedSums = {5, 11, 19, 23, 35, 50, -9223372036854775759L, -9223372036854775755L};

            assertAll(
                    () -> assertArrayEquals(expectedMaxes, result.first()),
                    () -> assertArrayEquals(expectedSums, result.second())
            );
        }

        record ChemicalSubstance(long mol) {
        }

        @Test
        void testRunningDoubleStatistics() {
            final var result = DoubleSequence.of(Math.E, Math.PI, 3, 6, 8, 12, 15, 16, 4)
                    .mapToObj(ElectricDevice::new)
                    .gather(runningDoubleStatisticsOf(ElectricDevice::current))
                    .teeing(longArrayOf(DoubleStatistics::getCount), doubleArrayOf(DoubleStatistics::getMax));

            final var expectedMaxes = new double[]{Math.E, Math.PI, Math.PI, 6, 8, 12, 15, 16, 16};

            assertAll(
                    () -> assertArrayEquals(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, result.first()),
                    () -> assertArrayEquals(expectedMaxes, result.second())
            );
        }

        record ElectricDevice(double current) {
        }
    }

    @Nested
    class WindowedTests {

        @ParameterizedTest(name = "Windows with size {0} an step {1} should be equal to the reference")
        @CsvSource(value = {"100, 1", "4, 1", "4, 3", "5, 4", "6, 2", "4, 5", "4, 100"})
        void testWindowedNoPartialWindows(final int size, final int step) {
            final var list = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

            final Function<List<Integer>, String> joinToString = w -> w.stream().map(String::valueOf).collect(joining());

            final var windows = list.stream()
                    .gather(windowed(size, step))
                    .map(joinToString)
                    .toList();

            final var reference = Sequence.of(list)
                    .windowed(size, step)
                    .map(w -> w.joinToString(""))
                    .toList();

            var byCollector = list.stream().collect(CollectorsX.windowed(size, step, false, joinToString));

            LOGGER.debug("{}", windows);

            assertEquals(reference, windows);
            assertEquals(reference, byCollector);
        }

        @Test
        void throwsIfContainsNullElement() {
            final var windows = Stream.of(0, 1, null, 3, 4, 5, 6, 7, 8, 9).gather(windowed(3));

            final var e = assertThrows(NullPointerException.class, windows::toList);
            assertThat(e.getMessage()).contains("must not be null");
        }

        @ParameterizedTest(name = "Windows with size {0} an step {1} should be equal to the reference")
        @CsvSource(value = {"100, 1", "4, 1", "4, 3", "5, 4", "6, 2", "4, 5", "4, 100"})
        void testWindowedPartialWindows(final int size, final int step) {
            final var list = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

            final var partialWindows = true;

            Function<List<Integer>, String> joinToString = w -> w.stream().map(String::valueOf).collect(joining());

            final var windows = list.stream()
                    .gather(windowed(size, step, partialWindows))
                    .map(joinToString)
                    .toList();

            final var reference = Sequence.of(list)
                    .windowed(size, step, partialWindows)
                    .map(w -> w.joinToString(""))
                    .toList();

            LOGGER.debug("{}", windows);

            var byCollector = list.stream().collect(CollectorsX.windowed(size, step, partialWindows, joinToString));

            assertEquals(reference, windows);
            assertEquals(reference, byCollector);
        }

        @ParameterizedTest(name = "Windows with size {0} an step {1} should be equal to the reference")
        @CsvSource(value = {
                "100, 1, true",
                "4, 1, false",
                "4, 3, false",
                "5, 4, true",
                "6, 2, true",
                "4, 5, true",
                "4, 100, false"
        })
        void testWindowedEmptyInput(final int size, final int step, final boolean partialWindows) {
            final var windows = Stream.empty()
                    .gather(windowed(size, step, partialWindows))
                    .map(w -> w.stream().map(String::valueOf).collect(joining()))
                    .toList();

            LOGGER.debug("{}", windows);

            assertThat(windows).isEmpty();
        }

        @Test
        void testChunked() {
            final var size = 14;
            final var list = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

            final var windows = list.stream()
                    .gather(chunked(size))
                    .map(w -> w.stream().map(String::valueOf).collect(joining()))
                    .toList();

            final var reference = Sequence.of(list)
                    .chunked(size)
                    .map(w -> w.joinToString(""))
                    .toList();

            LOGGER.debug("{}", windows);

            assertEquals(reference, windows);
        }

        @Test
        void testChunkedSingleElement() {
            final var size = 14;
            final var list = List.of(1.618);

            final var windows = list.stream()
                    .gather(chunked(size))
                    .map(w -> w.stream().map(String::valueOf).collect(joining()))
                    .toList();

            final var reference = Sequence.of(list)
                    .chunked(size)
                    .map(w -> w.joinToString(""))
                    .toList();

            assertEquals(reference, windows);
        }
    }
}
