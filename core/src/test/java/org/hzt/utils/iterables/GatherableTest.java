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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class GatherableTest {

    @Test
    void testMap() {
        final var sequence = Sequence.of("Hallo", "dit", "is", "een", "test");

        final var sum = sequence
                .gather(map(String::length))
                .reduce(0, Integer::sum);

        assertEquals(17, sum);
    }

    @Test
    void testLaziness() {
        final var sequence = Sequence.of("Hallo", "dit", "is", "een", "test");

        final var actual = new ArrayList<String>();
        sequence
                .onEach(actual::add)
                .gather(map(String::length));


        final var expected = new ArrayList<String>();
        sequence
                .onEach(expected::add)
                .map(String::length);

        assertEquals(expected, actual);
    }

    @Test
    void testMapMultiToList() {
        final var list = List.of(List.of("Hallo", "dit"), new HashSet<>(List.of("is", "een", "zekere")),
                new ArrayDeque<>(Set.of("test")));

        final var result = Sequence.of(list)
                .gather(GatherersX.<Iterable<String>, String>mapMulti(Iterable::forEach))
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        final var expected = Sequence.of(list)
                .<String>mapMulti(Iterable::forEach)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        assertAll(
                () -> assertEquals(List.of("Hallo", "zekere", "test"), result),
                () -> assertEquals(expected, result)
        );
    }

    @Test
    void testFlattenToList() {
        final var list = List.of(List.of("Hallo", "dit"), new HashSet<>(List.of("is", "een", "zekere")),
                new ArrayDeque<>(Set.of("test")));

        final var result = Sequence.of(list)
                .gather(flatten())
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        final var expected = Sequence.of(list)
                .flatMap(e -> e)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        assertAll(
                () -> assertEquals(List.of("Hallo", "zekere", "test"), result),
                () -> assertEquals(expected, result)
        );
    }

    private static final class Tests implements Gatherable<String> {

        private final List<String> testNames;

        public Tests(final String... testNames) {
            this.testNames = List.of(testNames);
        }

        @Override
        public Iterator<String> iterator() {
            return testNames.iterator();
        }
    }

    @Test
    void testCustomGatherable() {
        final var subjects = new Tests("Greek", "Latin", "French")
                .gather(windowSliding(2).andThen(map(s -> Sequence.of(s).joinToString())))
                .toList();

        assertEquals(List.of("Greek, Latin", "Latin, French"), subjects);
    }

    @Test
    void testFlatmapToList() {
        final var list = List.of(
                List.of("Hallo", "dit"),
                new HashSet<>(List.of("is", "een", "zekere")),
                new ArrayDeque<>(Set.of("test"))
        );

        final var result = Sequence.of(list)
                .map(Texts::new)
                .gather(flatMap(Texts::strings))
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        final var expected = Sequence.of(list)
                .map(Texts::new)
                .flatMap(Texts::strings)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toList();

        assertAll(
                () -> assertEquals(List.of("Hallo", "zekere", "test"), result),
                () -> assertEquals(expected, result)
        );
    }

    record Texts(Iterable<String> strings) {
    }

    @Test
    void testLimit() {
        final var integers = Sequence.iterate(1, i -> i * 2)
                .gather(limit(10))
                .toList();

        final var expected = List.of(1, 2, 4, 8, 16, 32, 64, 128, 256, 512);

        assertEquals(expected, integers);
    }

    @Test
    void testSkip() {
        final var integers = Sequence.iterate(1, i -> i * 2)
                .take(10)
                .gather(skip(5))
                .collect(toList());

        final var reference = Stream.iterate(1, i -> i * 2)
                .limit(10)
                .skip(5)
                .collect(toList());

        final var expected = List.of(32, 64, 128, 256, 512);

        assertAll(
                () -> assertEquals(expected, integers),
                () -> assertEquals(expected, reference)
        );
    }

    @Test
    void testGatherAndThenDifferentOrders() {
        final var integers = Sequence.iterate(1, i -> i * 2)
                .gather(map(String::valueOf)
                        .andThen(limit(10))
                        .andThen(map(s -> s)))
                .toList();

        final var integers2 = Sequence.iterate(1, i -> i * 2)
                .gather(limit(10).andThen(map(String::valueOf)))
                .toList();

        final var expected = List.of("1", "2", "4", "8", "16", "32", "64", "128", "256", "512");

        assertAll(
                () -> assertEquals(expected, integers),
                () -> assertEquals(expected, integers2)
        );
    }

    @Test
    void limitBeforeGather() {
        final var strings = Sequence.iterate(1, i -> i * 2)
                .take(10)
                .gather(map(String::valueOf))
                .toList();

        assertEquals(List.of("1", "2", "4", "8", "16", "32", "64", "128", "256", "512"), strings);
    }

    @Test
    void limitBeforeAndAfterGatherWithInfiniteStream() {
        final var strings = Sequence.iterate(1, i -> i * 2)
                .take(100)
                .gather(map(String::valueOf))
                .take(10)
                .toList();

        assertEquals(List.of("1", "2", "4", "8", "16", "32", "64", "128", "256", "512"), strings);
    }

    @Test
    void limitAfterGatherInInfiniteStreamTerminating() {
        final var count = 10;
        final var list = Sequence.iterate(1, i -> i * 2)
                .gather(mapIndexed((index, power) -> index + ", " + power))
                .take(count)
                .toList();

        assertEquals(List.of("0, 1", "1, 2", "2, 4", "3, 8", "4, 16", "5, 32", "6, 64", "7, 128", "8, 256", "9, 512"), list);
    }

    @Test
    void testGatherChunked() {
        final var windows = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .gather(windowFixed(4))
                .map(ListX::of)
                .toList();

        final var expected = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .chunked(4)
                .toList();

        assertEquals(expected, windows);
    }

    @Test
    void testCollectChunkedChained() {
        final var windows = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .gather(Gatherers.<Integer>windowFixed(4)
                        .andThen(map(List::size)))
                .toList();

        final var expected = Sequence.iterate(0, i -> i + 1)
                .take(10)
                .chunked(4)
                .map(CollectionX::size)
                .toList();

        assertEquals(expected, windows);
    }

    @Test
    void testSlidingWindowLimitAfterGather() {
        final var windows = Sequence.iterate(0, i -> i + 1)
                .gather(windowSliding(4))
                .take(4)
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final var windowsByMapMulti = Sequence.iterate(0, i -> i + 1)
                .mapMulti(slidingWindow(4))
                .take(4)
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final var expected = List.of("0123", "1234", "2345", "3456");

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
                    final var oldWindow = window;
                    final var newWindow = new Object[windowSize];
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
        final var noWindows = Sequence.iterate(0, i -> i + 1)
                .take(3)
                .mapMulti(slidingWindow(4))
                .none();

        assertTrue(noWindows);
    }

    @Test
    void testSlidingWindowLimitBeforeGather() {
        final var windows = Sequence.iterate(0, i -> i + 1)
                .take(11)
                .gather(windowSliding(4))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final var windowsByExtension = Sequence.iterate(0, i -> i + 1)
                .take(11)
                .then(IterableExtensions.windowed(4))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        assertAll(
                () -> assertEquals(List.of("0123", "1234", "2345", "3456", "4567", "5678", "6789", "78910"), windows),
                () -> assertEquals(windows, windowsByExtension)
        );
    }

    @Test
    void testSlidingWindowLargerThanSequence() {
        final var windows = Sequence.iterate(0, i -> i + 1)
                .take(6)
                .gather(windowSliding(7))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        final var windowsByExtension = Sequence.iterate(0, i -> i + 1)
                .take(6)
                .then(IterableExtensions.windowSliding(7))
                .map(s -> Sequence.of(s).joinToString(""))
                .toList();

        assertAll(
                () -> assertEquals(List.of("012345"), windows),
                () -> assertEquals(windows, windowsByExtension));
    }

    @Test
    void testScan() {
        final var integers = Sequence.iterate(1, i -> i * 2)
                .take(20)
                .gather(scan(() -> 10, Integer::sum))
                .toList();

        final var expected = List.of(11, 13, 17, 25, 41, 73, 137, 265, 521, 1033, 2057, 4105, 8201, 16393, 32777, 65545, 131081, 262153, 524297, 1048585);

        assertEquals(expected, integers);
    }

    @Test
    void testFold() {
        final var integers = Sequence.iterate(1, i -> i * 2)
                .take(20)
                .gather(fold(() -> 10, Integer::sum))
                .toList();

        final var expected = List.of(1048585);

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
        Reading(final Instant obtainedAt, final int kelvins) {
            this.obtainedAt = obtainedAt;
            this.kelvins = kelvins;
        }

        Reading(final String time, final int kelvins) {
            this(Instant.parse(time), kelvins);
        }

        boolean isSuspicious(final Reading next) {
            return next.obtainedAt.isBefore(obtainedAt.plusSeconds(5))
                   && (next.kelvins > kelvins + 30
                       || next.kelvins < kelvins - 30);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final var reading = (Reading) o;
            return kelvins == reading.kelvins && Objects.equals(obtainedAt, reading.obtainedAt);
        }

        @Override
        public int hashCode() {
            return Objects.hash(obtainedAt, kelvins);
        }
    }

    @Test
    void testZippedWithNextFilterSuspiciousReading() {
        final var readings = Sequence.of(
                new Reading("2023-09-21T10:15:30.00Z", 310),
                new Reading("2023-09-21T10:15:31.00Z", 312),
                new Reading("2023-09-21T10:15:32.00Z", 350),
                new Reading("2023-09-21T10:15:33.00Z", 310)
        );
        final var firstSuspicious = readings
                .gather(filterZippedWithNext(Reading::isSuspicious))
                .map(l -> l.get(1))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        assertEquals(new Reading("2023-09-21T10:15:32.00Z", 350), firstSuspicious);
    }
}
