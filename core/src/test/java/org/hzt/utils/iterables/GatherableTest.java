package org.hzt.utils.iterables;

import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.gatherers.Gatherers;
import org.hzt.utils.gatherers.GatherersX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hzt.utils.gatherers.Gatherers.fold;
import static org.hzt.utils.gatherers.Gatherers.scan;
import static org.hzt.utils.gatherers.Gatherers.unmodifiableListCopyOf;
import static org.hzt.utils.gatherers.Gatherers.windowFixed;
import static org.hzt.utils.gatherers.Gatherers.windowSliding;
import static org.hzt.utils.gatherers.GatherersX.filterZippedWithNext;
import static org.hzt.utils.gatherers.GatherersX.flatMap;
import static org.hzt.utils.gatherers.GatherersX.flatten;
import static org.hzt.utils.gatherers.GatherersX.limit;
import static org.hzt.utils.gatherers.GatherersX.map;
import static org.hzt.utils.gatherers.GatherersX.mapIndexed;
import static org.hzt.utils.gatherers.GatherersX.skip;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GatherableTest {

    @Test
    void testMap() {
        final Sequence<String> sequence = Sequence.of("Hallo", "dit", "is", "een", "test");

        final Integer sum = sequence
                .gather(map(String::length))
                .reduce(0, Integer::sum);

        assertEquals(17, sum);
    }

    @Test
    void testLaziness() {
        final Sequence<String> sequence = Sequence.of("Hallo", "dit", "is", "een", "test");

        final ArrayList<String> actual = new ArrayList<>();
        sequence
                .onEach(actual::add)
                .gather(map(String::length));


        final ArrayList<String> expected = new ArrayList<>();
        sequence
                .onEach(expected::add)
                .map(String::length);

        assertEquals(expected, actual);
    }

    @Test
    void testMapMultiToList() {
        final List<Collection<String>> list = Arrays.asList(Arrays.asList("Hallo", "dit"), new HashSet<>(Arrays.asList("is", "een", "zekere")),
                new ArrayDeque<>(Collections.singleton("test")));

        final List<String> result = Sequence.of(list)
                .gather(GatherersX.<Iterable<String>, String>mapMulti(Iterable::forEach))
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        final List<String> expected = Sequence.of(list)
                .<String>mapMulti(Iterable::forEach)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        assertAll(
                () -> assertIterableEquals(Arrays.asList("Hallo", "zekere", "test"), result),
                () -> assertIterableEquals(expected, result)
        );
    }

    @Test
    void testFlattenToList() {
        final List<Collection<String>> list = Arrays.asList(Arrays.asList("Hallo", "dit"), new HashSet<>(Arrays.asList("is", "een", "zekere")),
                new ArrayDeque<>(Collections.singleton("test")));

        final List<String> result = Sequence.of(list)
                .gather(flatten())
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        final List<String> expected = Sequence.of(list)
                .flatMap(e -> e)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        assertAll(
                () -> assertIterableEquals(Arrays.asList("Hallo", "zekere", "test"), result),
                () -> assertIterableEquals(expected, result)
        );
    }

    private static final class Tests implements Gatherable<String> {

        private final List<String> testNames;

        public Tests(String... testNames) {
            this.testNames = Collections.unmodifiableList(Arrays.asList(testNames));
        }

        @Override
        public Iterator<String> iterator() {
            return testNames.iterator();
        }
    }

    @Test
    void testCustomGatherable() {
        final List<String> ints = new Tests("Greek", "Latin", "French")
                .gather(windowSliding(2))
                .gather(map(s -> Sequence.of(s).joinToString()))
                .toList();

        assertIterableEquals(ListX.of("Greek, Latin", "Latin, French"), ints);
    }

    @Test
    void testFlatmapToList() {
        final List<Collection<String>> list = Arrays.asList(
                Arrays.asList("Hallo", "dit"),
                new HashSet<>(Arrays.asList("is", "een", "zekere")),
                new ArrayDeque<>(Collections.singleton("test"))
        );

        final List<String> result = Sequence.of(list)
                .map(Texts::new)
                .gather(flatMap(Texts::strings))
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        final List<String> expected = Sequence.of(list)
                .map(Texts::new)
                .flatMap(Texts::strings)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        assertAll(
                () -> assertIterableEquals(Arrays.asList("Hallo", "zekere", "test"), result),
                () -> assertIterableEquals(expected, result)
        );
    }

    static final class Texts {
        private final Iterable<String> strings;

        Texts(Iterable<String> strings) {
            this.strings = strings;
        }

        public Iterable<String> strings() {
            return strings;
        }
    }

    @Test
    void testLimit() {
        final List<Integer> integers = Sequence.iterate(1, i -> i * 2)
                .gather(limit(10))
                .toList();

        final List<Integer> expected = Arrays.asList(1, 2, 4, 8, 16, 32, 64, 128, 256, 512);

        assertEquals(expected, integers);
    }

    @Test
    void testSkip() {
        final List<Integer> integers = Sequence.iterate(1, i -> i * 2)
                .take(10)
                .gather(skip(5))
                .collect(toList());

        final List<Integer> reference = Stream.iterate(1, i -> i * 2)
                .limit(10)
                .skip(5)
                .collect(toList());

        final List<Integer> expected = Arrays.asList(32, 64, 128, 256, 512);

        assertAll(
                () -> assertEquals(expected, integers),
                () -> assertEquals(expected, reference)
        );
    }

    @Test
    void testGatherAndThenDifferentOrders() {
        final List<String> integers = Sequence.iterate(1, i -> i * 2)
                .gather(map(String::valueOf)
                        .andThen(limit(10))
                        .andThen(map(s -> s)))
                .toList();

        final List<String> integers2 = Sequence.iterate(1, i -> i * 2)
                .gather(limit(10).andThen(map(String::valueOf)))
                .toList();

        final List<String> expected = Arrays.asList("1", "2", "4", "8", "16", "32", "64", "128", "256", "512");

        assertAll(
                () -> assertEquals(expected, integers),
                () -> assertEquals(expected, integers2)
        );
    }

    @Test
    void limitBeforeGather() {
        final List<String> strings = Sequence.iterate(1, i -> i * 2)
                .take(10)
                .gather(map(String::valueOf))
                .toList();

        assertEquals(Arrays.asList("1", "2", "4", "8", "16", "32", "64", "128", "256", "512"), strings);
    }

    @Test
    void limitBeforeAndAfterGatherWithInfiniteStream() {
        final List<String> strings = Sequence.iterate(1, i -> i * 2)
                .take(100)
                .gather(map(String::valueOf))
                .take(10)
                .toList();

        assertEquals(Arrays.asList("1", "2", "4", "8", "16", "32", "64", "128", "256", "512"), strings);
    }

    @Test
    void limitAfterGatherInInfiniteStreamTerminating() {
        final int count = 10;
        final List<String> list = Sequence.iterate(1, i -> i * 2)
                .gather(mapIndexed((index, i) -> String.valueOf(i)))
                .take(count)
                .toList();

        assertEquals(Arrays.asList("1", "2", "4", "8", "16", "32", "64", "128", "256", "512"), list);
    }

    @Test
    void testGatherChunked() {
        final List<List<Integer>> windows = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .gather(windowFixed(4))
                .toList();

        final List<ListX<Integer>> expected = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .chunked(4)
                .toList();

        assertIterableEquals(expected, windows);
    }

    @Test
    void testCollectChunkedChained() {
        final List<Integer> windows = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .gather(Gatherers.<Integer>windowFixed(4)
                        .andThen(map(List::size)))
                .toList();

        final List<Integer> expected = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .chunked(4)
                .map(CollectionX::size)
                .toList();

        assertIterableEquals(expected, windows);
    }

    @Test
    void testSlidingWindowLimitAfterGather() {
        final List<String> windows = Sequence.iterate(0, i -> i + 1)
                .gather(windowSliding(4))
                .take(4)
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final List<String> windowsByMapMulti = Sequence.iterate(0, i -> i + 1)
                .mapMulti(slidingWindow(4))
                .take(4)
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final List<String> expected = Arrays.asList("0123", "1234", "2345", "3456");

        assertAll(
                () -> assertEquals(expected, windows),
                () -> assertEquals(expected, windowsByMapMulti)
        );
    }

    /**
     * Poc for a sliding window function using mapMulti
     */
    public static <TR> BiConsumer<TR, Consumer<List<TR>>> slidingWindow(final int windowSize) {
        if (windowSize < 1) {
            throw new IllegalArgumentException("'windowSize' must be greater than zero");
        }
        class SlidingWindow {
            Object[] window = new Object[windowSize];
            int at = 0;

            void nextWindow(final TR element, final Consumer<List<TR>> downstream) {
                window[at++] = element;
                if (at >= windowSize) {
                    final Object[] oldWindow = window;
                    final Object[] newWindow = new Object[windowSize];
                    System.arraycopy(oldWindow, 1, newWindow, 0, windowSize - 1);
                    window = newWindow;
                    at -= 1;
                    downstream.accept(unmodifiableListCopyOf(oldWindow));
                }
            }
        }
        return new SlidingWindow()::nextWindow;
    }

    @Test
    void windowLargerThanSequence() {
        final boolean noWindows = Sequence.iterate(0, i -> i + 1)
                .take(3)
                .mapMulti(slidingWindow(4))
                .none();

        assertTrue(noWindows);
    }

    @Test
    void testSlidingWindowLimitBeforeGather() {
        final List<String> windows = Sequence.iterate(0, i -> i + 1)
                .take(11)
                .gather(windowSliding(4))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final List<String> windowsByExtension = Sequence.iterate(0, i -> i + 1)
                .take(11)
                .then(IterableExtensions.windowed(4))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        assertAll(
                () -> assertIterableEquals(Arrays.asList("0123", "1234", "2345", "3456", "4567", "5678", "6789", "78910"), windows),
                () -> assertIterableEquals(windows, windowsByExtension)
        );
    }

    @Test
    void testSlidingWindowLargerThanSequence() {
        final List<String> windows = Sequence.iterate(0, i -> i + 1)
                .take(6)
                .gather(windowSliding(7))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final List<String> windowsByExtension = Sequence.iterate(0, i -> i + 1)
                .take(6)
                .then(IterableExtensions.windowSliding(7))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        assertAll(
                () -> assertIterableEquals(Collections.singletonList("012345"), windows),
                () -> assertIterableEquals(windows, windowsByExtension));
    }

    @Test
    void testScan() {
        final List<Integer> integers = Sequence.iterate(1, i -> i * 2)
                .take(20)
                .gather(scan(() -> 10, Integer::sum))
                .toList();

        final List<Integer> expected = Arrays.asList(11, 13, 17, 25, 41, 73, 137, 265, 521, 1033, 2057, 4105, 8201, 16393, 32777, 65545, 131081, 262153, 524297, 1048585);

        assertEquals(expected, integers);
    }

    @Test
    void testFold() {
        final List<Integer> integers = Sequence.iterate(1, i -> i * 2)
                .take(20)
                .gather(fold(() -> 10, Integer::sum))
                .toList();

        final List<Integer> expected = Collections.singletonList(1048585);

        assertEquals(expected, integers);
    }

    /**
     * Example from jep 461
     *
     * @see <a href="https://openjdk.org/jeps/461">Example: Embracing the stream</a>
     */
    static final class Reading {
        private final Instant obtainedAt;
        private final int kelvins;

        /**
         *
         */
        Reading(Instant obtainedAt, int kelvins) {
            this.obtainedAt = obtainedAt;
            this.kelvins = kelvins;
        }

        Reading(String time, int kelvins) {
            this(Instant.parse(time), kelvins);
        }

        boolean isSuspicious(Reading next) {
            return next.obtainedAt.isBefore(obtainedAt.plusSeconds(5))
                   && (next.kelvins > kelvins + 30
                       || next.kelvins < kelvins - 30);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Reading reading = (Reading) o;
            return kelvins == reading.kelvins && Objects.equals(obtainedAt, reading.obtainedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(obtainedAt, kelvins);
        }
    }

    @Test
    void testZippedWithNextFilterSuspiciousReading() {
        final Sequence<Reading> readings = Sequence.of(
                new Reading("2023-09-21T10:15:30.00Z", 310),
                new Reading("2023-09-21T10:15:31.00Z", 312),
                new Reading("2023-09-21T10:15:32.00Z", 350),
                new Reading("2023-09-21T10:15:33.00Z", 310)
        );
        final Reading firstSuspicious = readings
                .gather(filterZippedWithNext(Reading::isSuspicious))
                .map(l -> l.get(1))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        assertEquals(new Reading("2023-09-21T10:15:32.00Z", 350), firstSuspicious);
    }
}
