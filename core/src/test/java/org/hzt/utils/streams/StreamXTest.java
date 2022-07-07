package org.hzt.utils.streams;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static org.hzt.utils.collectors.CollectorsX.toUnmodifiableList;
import static org.junit.jupiter.api.Assertions.*;

class StreamXTest {

    @Test
    void mapFilterReduce() {
        List<String> strings = Arrays.asList("This", "is", "a", "StreamX", "test");

        final Integer lengthSum = StreamX.of(strings)
                .filter("This is a test"::contains)
                .map(String::length)
                .reduce(0, Integer::sum);

        assertEquals(11, lengthSum);
    }

    @Test
    void testStreamXIsLazyAndSequential() {
        List<String> strings = Arrays.asList("This", "is", "a", "StreamX", "test");

        final StreamX<Integer> streamX = StreamX.of(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertFalse(streamX.isParallel());
    }

    @Test
    void testStreamXIsLazyAndParallel() {
        List<String> strings = Arrays.asList("This", "is", "a", "StreamX", "test");

        final StreamX<Integer> streamX = StreamX.parallel(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertTrue(streamX.isParallel());
    }

    @Test
    void testStreamXCanBeConsumedOnlyOnce() {
        List<String> strings = Arrays.asList("This", "is", "a", "StreamX", "test");

        final StreamX<String> stream = StreamX.of(strings);

        final Optional<String> max = stream
                .filter("This is a test"::contains)
                .maxBy(String::length);

        System.out.println("stream = " + stream);

        assertAll(
                () -> assertEquals("This", max.orElseThrow(NoSuchElementException::new)),
                () -> assertThrows(IllegalStateException.class, () -> stream.anyMatch(String::isEmpty))
        );
    }

    private boolean contained(String s) {
        fail();
        return "This is a test".contains(s);
    }

    @Test
    void streamXCanBeImplementedAsFunctionInterface() {
        final String firstNameLastInAlphabet = new Gallery()
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
        Spliterator<String> spliterator = new Spliterator<String>() {
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
    void testGroupBy() {
        final List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        final UnaryOperator<Integer> modulo3 = i -> i % 3;

        final Map<Integer, List<Integer>> expected = integers.stream()
                .collect(groupingBy(modulo3));

        final Map<Integer, List<Integer>> actual = StreamX.of(integers).groupBy(modulo3);

        assertEquals(expected, actual);
    }

    @Test
    void testLoopOverAStreamX() {
        final IntStream iterate = IntStream.iterate(0, i -> ++i);
        final StreamX<String> strings = StreamX.of(iterate
                        .limit(1_000_000)
                .boxed())
                .map(String::valueOf);

        int counter = 0;
        //noinspection unused
        for (String s : strings) {
            counter++;
        }

        assertEquals(1_000_000, counter);
    }

    @Test
    void testParallelism() {
        final Sequence<LocalDate> generate = Sequence.generate(LocalDate.ofEpochDay(0), date -> date.plusWeeks(2))
                .takeWhile(LocalDate.of(2020, Month.JANUARY, 1)::isAfter);

        final ListX<Month> months = StreamX.of(generate)
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
        final List<Museum> museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        final List<Painting> expected = museumListContainingNulls.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .collect(toUnmodifiableList());

        final List<Painting> paintings = StreamX.of(museumListContainingNulls)
                .flatMapIterable(Museum::getPaintings)
                .toList();

        assertEquals(expected, paintings);
    }

}
