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

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.hzt.utils.streams.StreamExtensions.chunked;
import static org.hzt.utils.streams.StreamExtensions.filter;
import static org.hzt.utils.streams.StreamExtensions.map;
import static org.hzt.utils.streams.StreamExtensions.peek;
import static org.hzt.utils.streams.StreamExtensions.scan;
import static org.hzt.utils.streams.StreamExtensions.windowed;
import static org.hzt.utils.streams.StreamFinishers.fold;
import static org.hzt.utils.streams.StreamFinishers.toSet;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class StreamXTest {

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
                .peek(s -> fail());

        assertFalse(streamX.isParallel());
    }

    @Test
    void testStreamXIsLazyAndParallel() {
        final var strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.parallel(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertTrue(streamX.isParallel());
    }

    @Test
    void testStreamXCanBeConsumedOnlyOnce() {
        final var strings = List.of("This", "is", "a", "StreamX", "test");

        final var stream = StreamX.of(strings);

        final var max = stream
                .filter("This is a test"::contains)
                .maxBy(String::length);

        System.out.println("stream = " + stream);

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
                .collect(toUnmodifiableList());

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
                .isParallel(System.out::println)
                .parallel()
                .isParallel(System.out::println)
                .filter(this::dateInLeapYear)
                .map(this::dateToMonth)
                .toListX();

        assertEquals(314, months.size());

    }

    private boolean dateInLeapYear(final LocalDate localDate) {
        System.out.println("dateInLeapYear:");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        return localDate.isLeapYear();
    }

    private Month dateToMonth(final LocalDate localDate) {
        System.out.println("dateToMonth");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        return localDate.getMonth();
    }

    @Test
    void testFlatMapIterable() {
        final var museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumListContainingNulls.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .collect(toUnmodifiableList());

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

            System.out.println(expected);

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
            final var actualIterations = new AtomicInteger();
            final var expectedIterations = new AtomicInteger();

            final var windows = StreamX.iterate(0, i -> i + 1)
                    .peek(e -> actualIterations.incrementAndGet())
                    .then(windowed(4, (List<Integer> window) -> window)
                            .andThen(peek(System.out::println))
                            .andThen(scan(1, (acc, t) -> acc + t.size()))
                            .andThen(map(String::valueOf))
                            .andThen(filter(s -> s.length() == 3)))
                    .findFirst();

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .onEach(e -> expectedIterations.incrementAndGet())
                    .windowed(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .map(String::valueOf)
                    .filter(s -> s.length() == 3)
                    .findFirst();

            System.out.println(expected);

            assertAll(
                    () -> assertEquals(29, actualIterations.get()),
                    () -> assertEquals(expectedIterations.get(), actualIterations.get() - 1),
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

    @Nested
    class StreamFinisherTests {

        @Test
        void finishByFold() {
            final var windows = StreamX.iterate(0, i -> i + 1)
                    .limit(10)
                    .finish(fold(new StringBuilder(), StringBuilder::append))
                    .toString();

            final var expected = Sequence.iterate(0, i -> i + 1).take(10).joinToString("");

            assertEquals(expected, windows);
        }

        @Test
        void finishByExtendedFinisher() {
            final var set = StreamX.iterate(0, i -> i + 1)
                    .limit(10)
                    .finish(StreamExtensions.<Integer>windowed(2)
                            .andThen(scan(0, (sum, window) -> sum + window.size()))
                            .finish(toSet()));

            final var expected = Sequence.iterate(0, i -> i + 1)
                    .take(10)
                    .windowed(2)
                    .scan(0, (sum, window) -> sum + window.size())
                    .toSet();

            assertEquals(expected, set);
        }
    }
}
