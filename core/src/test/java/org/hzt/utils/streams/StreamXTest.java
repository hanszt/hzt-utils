package org.hzt.utils.streams;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static org.hzt.utils.streams.StreamExtensions.*;
import static org.junit.jupiter.api.Assertions.*;

class StreamXTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamXTest.class);

    @Test
    void mapFilterReduce() {
        final var strings = List.of("This", "is", "a", "StreamX", "test");

        final var lengthSum = StreamX.of(strings)
                .filter("This is a test"::contains)
                .map(String::length)
                .reduce(0, Integer::sum);

        assertEquals(11, lengthSum);
    }

    @Test
    void testStreamXIsLazyAndSequential() {
        final var strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.of(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(_ -> fail());

        assertFalse(streamX.isParallel());
    }

    @Test
    void testStreamXIsLazyAndParallel() {
        final var strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.parallel(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(_ -> fail());

        assertTrue(streamX.isParallel());
    }

    @Test
    void testStreamXCanBeConsumedOnlyOnce() {
        final var strings = List.of("This", "is", "a", "StreamX", "test");

        final var stream = StreamX.of(strings);

        final var max = stream
                .filter("This is a test"::contains)
                .maxBy(String::length);

        LOGGER.atDebug().setMessage(() -> "stream = " + stream).log();

        //noinspection DataFlowIssue
        assertAll(
                () -> assertEquals("This", max.orElseThrow()),
                () -> assertThrows(IllegalStateException.class, () -> stream.anyMatch(String::isEmpty))
        );
    }

    private boolean contained(final String s) {
        fail();
        return "This is a test".contains(s);
    }

    @Test
    void streamXCanBeImplementedAsFunctionInterface() {
        final var firstNameLastInAlphabet = new Gallery()
                .map(Painting::painter)
                .maxOf(Painter::getFirstName);

        assertEquals("Vincent", firstNameLastInAlphabet);
    }

    private static class Gallery implements StreamX<Painting> {

        private final List<Painting> paintings = TestSampleGenerator.createPaintingList();

        @Override
        public Spliterator<Painting> spliterator() {
            return paintings.spliterator();
        }
    }

    @Test
    void testHasCharacteristics() {
        final Spliterator<String> spliterator = new Spliterator<>() {
            @Override
            public boolean tryAdvance(final Consumer<? super String> action) {
                return false;
            }

            @Override
            public Spliterator<String> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SUBSIZED;
            }
        };
        assertAll(
                () -> assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED) || spliterator.hasCharacteristics(Spliterator.SUBSIZED)),
                () -> assertTrue(spliterator.hasCharacteristics(Spliterator.ORDERED) || spliterator.hasCharacteristics(Spliterator.CONCURRENT))
        );
    }

    @Test
    void testTakeWhile() {
        final var integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        final var expected = integers.stream()
                .takeWhile(i -> i < 7)
                .toList();

        final var actual = StreamX.of(integers)
                .takeWhile(i -> i < 7)
                .toList();

        assertEquals(expected, actual);
    }

    @Test
    void testGroupBy() {
        final var integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        final UnaryOperator<Integer> modulo3 = i -> i % 3;

        final var expected = integers.stream()
                .collect(groupingBy(modulo3));

        final var actual = StreamX.of(integers).groupBy(modulo3);

        assertEquals(expected, actual);
    }

    @Test
    void testLoopOverAStreamX() {
        final var strings = StreamX.of(IntStream.iterate(0, i -> i < 1_000_000, i -> ++i)
                        .boxed())
                .map(String::valueOf);

        var counter = 0;
        //noinspection unused
        for (final var s : strings) {
            counter++;
        }

        assertEquals(1_000_000, counter);
    }

    @Test
    void testParallelism() {
        final var generate = Sequence.iterate(LocalDate.EPOCH, date -> date.plusWeeks(2))
                .takeWhile(LocalDate.of(2020, Month.JANUARY, 1)::isAfter);

        final var months = StreamX.of(generate)
                .filter(this::dateInLeapYear)
                .parallel()
                .filter(this::dateInLeapYear)
                .map(this::dateToMonth)
                .toListX();

        assertEquals(314, months.size());

    }

    private boolean dateInLeapYear(final LocalDate localDate) {
        var inLeapYear = localDate.isLeapYear();
        LOGGER.trace("dateInLeapYear: {}", inLeapYear);
        LOGGER.atTrace().setMessage(() -> "Thread.currentThread().getName() = " + Thread.currentThread().getName()).log();
        return inLeapYear;
    }

    private Month dateToMonth(final LocalDate localDate) {
        LOGGER.atTrace().setMessage(() -> "dateToMonth").log();
        LOGGER.atTrace().setMessage(() -> "Thread.currentThread().getName() = " + Thread.currentThread().getName()).log();
        return localDate.getMonth();
    }

    @Test
    void testFlatMapIterable() {
        final var museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumListContainingNulls.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .toList();

        final var paintings = StreamX.of(museumListContainingNulls)
                .flatMapIterable(Museum::getPaintings)
                .toList();

        assertEquals(expected, paintings);
    }

    @Nested
    class ExtensionsTests {

        @Test
        void testWindowedExtension() {
            final var windows = StreamX.iterate(0, i -> i + 1)
                    .then(chunked(4))
                    .limit(10)
                    .toList();

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .chunked(4)
                    .take(10)
                    .map(Collectable::toList)
                    .toList();

            assertEquals(expected, windows);
        }

        @Test
        void mapConcurrentVsSequential() {
            var start = System.nanoTime();

            final var strings = StreamX.iterate(1, i -> i + 2)
                    .limit(10)
                    .then(mapConcurrent(10, this::toStringAndSleep))
                    .toList();

            var end = System.nanoTime();
            final var durationConcurrent = Duration.ofNanos(end - start);

            start = System.nanoTime();

            final var stringsRegularMap = Sequence.iterate(1, i -> i + 2)
                    .take(10)
                    .map(this::toStringAndSleep)
                    .toList();

            final var expected = List.of("1", "3", "5", "7", "9", "11", "13", "15", "17", "19");
            end = System.nanoTime();
            final var durationSequential = Duration.ofNanos(end - start);

            LOGGER.atDebug().setMessage(() -> "durationConcurrent = " + durationConcurrent).log();
            LOGGER.atDebug().setMessage(() -> "durationSequential = " + durationSequential).log();

            assertAll(
                    () -> assertTrue(durationConcurrent.compareTo(durationSequential) < 0),
                    () -> assertEquals(stringsRegularMap, strings),
                    () -> assertEquals(expected, strings)
            );
        }

        private String toStringAndSleep(int i) {
            try {
                Thread.sleep(Duration.ofMillis(10));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return String.valueOf(i);
        }

        @Test
        void extendedExtension() {
            final var windows = StreamX.iterate(0, i -> i + 1)
                    .then(StreamExtensions.<Integer>windowed(4)
                            .andThen(scan(1, (acc, t) -> acc + t.size()))
                            .andThen(map(String::valueOf)))
                    .limit(10)
                    .toList();

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .windowed(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .map(String::valueOf)
                    .take(10)
                    .toList();

            LOGGER.debug("Expected: {}", expected);

            assertEquals(expected, windows);
        }

        @Test
        void collectFromExtensionChain() {

            final var windows = Stream.iterate(0, i -> i + 1)
                    .limit(10)
                    .collect(StreamExtensions.<Integer>chunked(4)
                            .andThen(scan(1, (acc1, t1) -> acc1 + t1.size()))
                            .collect(Collectors.groupingBy(i1 -> i1 % 4)));

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .take(10)
                    .chunked(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .collect(Collectors.groupingBy(i -> i % 4));

            assertEquals(expected, windows);
        }

        @Test
        void shortCircuitExtension() {
            final var actualIterations = new Counter();
            final var expectedIterations = new Counter();

            final var windows = StreamX.iterate(0, i -> i + 1)
                    .peek(_ -> ++actualIterations.value)
                    .then(windowed(4, (List<Integer> window) -> window)
                            .andThen(peek(it -> LOGGER.trace("{}", it)))
                            .andThen(scan(1, (acc, t) -> acc + t.size()))
                            .andThen(map(String::valueOf))
                            .andThen(filter(s -> s.length() == 3)))
                    .findFirst();

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .onEach(_ -> ++expectedIterations.value)
                    .windowed(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .map(String::valueOf)
                    .filter(s -> s.length() == 3)
                    .findFirst();

            LOGGER.debug("Expected: {}", expected);

            assertAll(
                    () -> assertEquals(29, actualIterations.value),
                    () -> assertEquals(expectedIterations.value, actualIterations.value - 1),
                    () -> assertEquals(expected, windows)
            );
        }



        @Test
        void composedExtension() {
            final var windows = StreamX.iterate(0, i -> i + 1)
                    .then(StreamExtensions.<List<Integer>, Integer>scan(1, (acc, t) -> acc + t.size())
                            .compose(chunked(4)))
                    .limit(10)
                    .toList();

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .chunked(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .take(10)
                    .toList();

            assertEquals(expected, windows);
        }
    }

    private static final class Counter {
        int value = 0;
    }
}
