package org.hzt.utils.streams;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class StreamXTest {

    @Test
    void mapFilterReduce() {
        List<String> strings = List.of("This", "is", "a", "StreamX", "test");

        final var lengthSum = StreamX.of(strings)
                .filter("This is a test"::contains)
                .map(String::length)
                .reduce(0, Integer::sum);

        assertEquals(11, lengthSum);
    }

    @Test
    void testStreamXIsLazyAndSequential() {
        List<String> strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.of(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertFalse(streamX.isParallel());
    }

    @Test
    void testStreamXIsLazyAndParallel() {
        List<String> strings = List.of("This", "is", "a", "StreamX", "test");

        final var streamX = StreamX.parallel(strings)
                .filter(this::contained)
                .peek(Assertions::fail)
                .map(String::length)
                .peek(s -> fail());

        assertTrue(streamX.isParallel());
    }

    @Test
    void testStreamXCanNotBeConsumedMoreThanOnce() {
        List<String> strings = List.of("This", "is", "a", "StreamX", "test");

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
        final var firstNameLastInAlphabet = new Museum()
                .map(Painting::painter)
                .maxOf(Painter::getFirstName);

        assertEquals("Vincent", firstNameLastInAlphabet);
    }

    private static class Museum implements StreamX<Painting> {

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
                .collect(Collectors.toUnmodifiableList());

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
                .collect(Collectors.groupingBy(modulo3));

        final var actual = StreamX.of(integers).groupBy(modulo3);

        assertEquals(expected, actual);
    }

    @Test
    void testLoopOverAStreamX() {
        final var strings = StreamX.of(IntStream.iterate(0, i -> i < 1_000_000, i -> ++i)
                .boxed())
                .map(String::valueOf);

        int counter = 0;
        for (String s : strings) {
            counter++;
        }

        assertEquals(1_000_000, counter);
    }

}
