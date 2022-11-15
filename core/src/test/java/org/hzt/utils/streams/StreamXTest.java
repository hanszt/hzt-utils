package org.hzt.utils.streams;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.*;

class StreamXTest {

    @Test
    void mapFilterReduce() {
        var strings = List.of("This", "is", "a", "StreamX", "test");

        final var lengthSum = StreamX.of(strings)
                .filter("This is a test"::contains)
                .map(String::length)
                .reduce(0, Integer::sum);

        assertEquals(11, lengthSum);
    }

    @Test
    void testStreamXIsLazyAndSequential() {
        var strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.of(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertFalse(streamX.isParallel());
    }

    @Test
    void testStreamXIsLazyAndParallel() {
        var strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.parallel(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertTrue(streamX.isParallel());
    }

    @Test
    void testStreamXCanBeConsumedOnlyOnce() {
        var strings = List.of("This", "is", "a", "StreamX", "test");

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

    private boolean contained(String s) {
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
        public @NotNull Spliterator<Painting> spliterator() {
            return paintings.spliterator();
        }
    }

    @Test
    void testHasCharacteristics() {
        Spliterator<String> spliterator = new Spliterator<>() {
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
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
        for (var s : strings) {
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

    private boolean dateInLeapYear(LocalDate localDate) {
        System.out.println("dateInLeapYear:");
        System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        return localDate.isLeapYear();
    }

    private Month dateToMonth(LocalDate localDate) {
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

}
