package org.hzt.utils.gatherers;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.statistics.DoubleStatistics;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.statistics.LongStatistics;
import org.hzt.utils.streams.StreamX;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static org.hzt.utils.collectors.CollectorsX.doubleArrayOf;
import static org.hzt.utils.collectors.CollectorsX.intArrayOf;
import static org.hzt.utils.collectors.CollectorsX.longArrayOf;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class GatherersXTest {

    @Test
    void testDistinctBy() {
        final List<Organization> list = Sequence.of("KLM", "klm", "NS", "asml", "van gogh", "ASML", "ns")
                .map(Organization::new)
                .gather(distinctBy(s -> s.name.toLowerCase()))
                .toList();

        final List<Organization> expected = Sequence.of("KLM", "NS", "asml", "van gogh")
                .map(Organization::new)
                .toList();

        assertIterableEquals(expected, list);
    }

    private static final class Organization {

        private final String name;

        public Organization(final String name) {
            this.name = name;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Organization that = (Organization) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    @Test
    void testMapNotNull() {
        final MapX<String, String> courseRepo = MapX.of(
                "A", "Math",
                "F", "Science",
                "R", "History",
                "L", "Politics"
        );

        final String[] courseIds = {"A", null, "F", null, "R", "P", "Z", "T"};

        final ListX<String> result = Sequence.of(courseIds)
                .gather(mapNotNull(courseRepo::get))
                .toListX();

        final ListX<String> byMapMulti = Sequence.of(courseIds)
                .mapMulti(notNull(courseRepo::get))
                .toListX();

        assertAll(
                () -> assertEquals(ListX.of("Math", "Science", "History"), result),
                () -> assertEquals(byMapMulti, result)
        );
    }

    @Test
    void testDropWhile() {
        final List<Integer> result = StreamX.iterate(1, i1 -> i1 + 1)
                .limit(10)
                .gather(dropWhile(i -> i != 5))
                .toList();

        final List<Integer> takeWhileResult = StreamX.iterate(1, i1 -> i1 + 1)
                .limit(10)
                .dropWhile(i -> i != 5)
                .toList();

        final List<Integer> expected = Arrays.asList(5, 6, 7, 8, 9, 10);

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected, takeWhileResult)
        );
    }

    @Test
    void testTakeWhile() {
        final Sequence<Integer> integers = Sequence.iterate(1, i -> i + 1);

        final List<Integer> result = integers
                .gather(takeWhile(i -> i != 5))
                .toList();

        final List<Integer> takeWhileResult = integers
                .takeWhile(i -> i != 5)
                .toList();

        final List<Integer> expected = Arrays.asList(1, 2, 3, 4);

        assertAll(
                () -> assertEquals(expected, result),
                () -> assertEquals(expected, takeWhileResult)
        );
    }

    @Test
    void testTakeWhileInclusive() {
        final Predicate<Integer> isOdd = i -> i % 2 != 0;
        final Sequence<Integer> integers = Sequence.of(1, 3, 5, 6, 7, 8, 10, 12);

        final List<Integer> result = integers
                .gather(takeWhileIncluding(isOdd))
                .toList();

        final List<Integer> takeWhileResult = integers
                .takeWhile(isOdd)
                .toList();

        assertAll(
                () -> assertEquals(Arrays.asList(1, 3, 5, 6), result),
                () -> assertEquals(Arrays.asList(1, 3, 5), takeWhileResult)
        );
    }

    private static <T, R> BiConsumer<? super T, Consumer<R>> notNull(final Function<? super T, ? extends R> mapper) {
        return (item, consumer) -> GatherersX.acceptIfResultNotNull(mapper, item, consumer);
    }

    @Nested
    class SortedTests {

        @Test
        void testSortedBySelector() {

            final ListX<Integer> list = ListX.of(5, 6, 8, 4, 12, 15, 16, 4);

            final List<Person> result = Sequence.of(list)
                    .map(Person::new)
                    .gather(sortedBy(Person::age))
                    .toList();

            final List<Person> expected = Sequence.of(list)
                    .map(Person::new)
                    .sorted(comparing(Person::age))
                    .toList();

            assertIterableEquals(expected, result);
        }

        private final class Person {
            private final int age;

            Person(final int age) {
                this.age = age;
            }

            public int age() {
                return age;
            }

            @Override
            public boolean equals(final Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                final Person person = (Person) o;
                return age == person.age;
            }

            @Override
            public int hashCode() {
                return Objects.hash(age);
            }
        }

        @Test
        void testSortedDescendingBySelector() {
            final ListX<Integer> list = ListX.of(5, 6, 8, 4, 12, 15, 16, 4);

            final List<Person> result = list.stream()
                    .map(Person::new)
                    .gather(sortedDescendingBy(Person::age))
                    .toList();

            final List<Person> bySortedMethod = list.stream()
                    .map(Person::new)
                    .sorted(comparing(Person::age).reversed())
                    .toList();

            final List<Person> expected = StreamX.of(16, 15, 12, 8, 6, 5, 4, 4).map(Person::new).toList();

            assertAll(
                    () -> assertIterableEquals(expected, result),
                    () -> assertIterableEquals(bySortedMethod, result)
            );
        }

        @Test
        void testSorted() {
            final ListX<String> list = ListX.of("A", "B", "A", "F", "F", "R", "P", "Z", "T");

            final List<String> result = list.stream()
                    .gather(sorted())
                    .toList();

            final List<String> expected = list.stream()
                    .sorted()
                    .toList();

            assertEquals(expected, result);
        }

        @Test
        void testSortedDistinct() {
            final ListX<String> list = ListX.of("A", "B", "A", "F", "F", "R", "P", "Z", "T");

            final List<String> result = list.stream()
                    .gather(sortedDistinct())
                    .toList();

            final List<String> resultBySequence = Sequence.of(list)
                    .gather(sortedDistinct())
                    .toList();

            final List<String> expected = list.stream()
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
            final IntSequence integers = IntSequence.of(5, 6, 8, 4, 12, 15, 16, 4);

            final Pair<int[], long[]> result = integers
                    .mapToObj(Person::new)
                    .gather(runningIntStatisticsOf(Person::age))
                    .teeing(intArrayOf(IntStatistics::getMax), longArrayOf(IntStatistics::getSum));

            final long[] sums = integers
                    .boxed()
                    .mapMulti(runningStatistics())
                    .mapToLong(IntStatistics::getSum)
                    .toArray();

            final int[] expectedMaxes = {5, 6, 8, 8, 12, 15, 16, 16};
            final long[] expectedSums = {5, 11, 19, 23, 35, 50, 66, 70};

            assertAll(
                    () -> assertArrayEquals(expectedMaxes, result.first()),
                    () -> assertArrayEquals(expectedSums, result.second()),
                    () -> assertArrayEquals(expectedSums, sums)
            );
        }

        private BiConsumer<Integer, Consumer<IntStatistics>> runningStatistics() {
            final IntStatistics stats = new IntStatistics();
            return (value, consumer) -> {
                stats.accept(value);
                consumer.accept(new IntStatistics().combine(stats));
            };
        }

        private final class Person {
            private final int age;

            Person(final int age) {
                this.age = age;
            }

            public int age() {
                return age;
            }
        }

        @Test
        void testRunningLongStatistics() {
            final Pair<long[], long[]> result = LongSequence.of(5, 6, 8, 4, 12, 15, Long.MAX_VALUE, 4)
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

        final class ChemicalSubstance {
            private final long mol;

            ChemicalSubstance(final long mol) {
                this.mol = mol;
            }

            public long mol() {
                return mol;
            }
        }

        @Test
        void testRunningDoubleStatistics() {
            final Pair<long[], double[]> result = DoubleSequence.of(Math.E, Math.PI, 3, 6, 8, 12, 15, 16, 4)
                    .mapToObj(ElectricDevice::new)
                    .gather(runningDoubleStatisticsOf(ElectricDevice::current))
                    .teeing(longArrayOf(DoubleStatistics::getCount), doubleArrayOf(DoubleStatistics::getMax));

            final double[] expectedMaxes = {Math.E, Math.PI, Math.PI, 6, 8, 12, 15, 16, 16};

            assertAll(
                    () -> assertArrayEquals(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, result.first()),
                    () -> assertArrayEquals(expectedMaxes, result.second())
            );
        }

        final class ElectricDevice {
            private final double current;

            ElectricDevice(final double current) {
                this.current = current;
            }

            public double current() {
                return current;
            }
        }
    }
}
