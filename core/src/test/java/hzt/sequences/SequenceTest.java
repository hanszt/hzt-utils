package hzt.sequences;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.SetX;
import hzt.function.It;
import hzt.iterables.IterableXTest;
import hzt.strings.StringX;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {

    @Test
    void testSimpleStreamWithMapYieldsIteratorWithNext() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final Sequence<Integer> sequence = Sequence.of(list)
                .map(SequenceTest::lengthMappingNotCalledWhenNotConsumed);

        assertTrue(sequence.iterator().hasNext());
    }

    private static int lengthMappingNotCalledWhenNotConsumed(String s) {
        fail("Should only be called when consumed with terminal operation");
        return s.length();
    }

    @Test
    void testMapReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(17, sum);
    }

    @Test
    void testSimpleStreamWithFilterYieldsIteratorWithNext() {
        final ListX<Integer> list = ListX.of(1, 2, 3, 4, 5, 6);
        final Sequence<Integer> sequence = Sequence.of(list)
                .filter(SequenceTest::filterNotCalledWhenNotConsumed);

        assertNotNull(sequence);
    }

    private static boolean filterNotCalledWhenNotConsumed(int i) {
        fail("Should only be called when consumed with terminal operation");
        return i < 3;
    }

    @Test
    void testFilterReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final int sum = Sequence.of(list)
                .map(String::length)
                .filter(l -> l > 3)
                .reduce(Integer::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(9, sum);
    }

    @Test
    void testMapFilterReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(17, sum);
    }

    @Test
    void testFilterIndexed() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && index % 2 == 1)
                .sumOfLongs(It::self);

        assertEquals(6, sum);
    }

    @Test
    void testMapFilterReduceToList() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final ListX<Integer> result = list.asSequence()
                .map(String::length)
                .filter(l -> l > 3)
                .toListX();

        assertEquals(ListX.of(5, 4), result);
    }

    @Test
    void testMapFilterReduceToSet() {
        var list = ListX.of("Hallo", "dit", "is", "een", "test");
        final var result = list.asSequence()
                .map(String::length)
                .toSetX();

        assertEquals(SetX.of(2, 3, 4, 5), result);
    }

    @Test
    void testFlatMapToList() {
        final ListX<Iterable<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final ListX<String> result = list.asSequence()
                .flatMap(It::self)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        assertIterableEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testCollectGroupingBy() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final Map<Painter, List<Painting>> expected = museumList.stream()
                .flatMap(m -> m.getPaintings().stream())
                .collect(Collectors.groupingBy(Painting::painter));

        final Map<Painter, List<Painting>> actual = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .collect(Collectors.groupingBy(Painting::painter));

        final MapX<Painter, MutableListX<Painting>> actual2 = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .groupBy(Painting::painter);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected, actual2)
        );
    }

    @Test
    void testGenerateSequence() {
        final ListX<String> strings = Sequence.generate(0, i -> i + 1)
                .take(12)
                .filter(i -> i % 2 == 0)
                .map(IterableXTest::compute200Millis)
                .onEach(System.out::println)
                .toListX();

        assertEquals(6, strings.size());
    }

    @Test
    void testLargeSequence() {
        final MutableListX<BigDecimal> bigDecimals = Sequence.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .toDescendingSortedMutableListOf(BigDecimal::valueOf);

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testTakeWhile() {
        final ListX<String> strings = Sequence.generate(0, i -> i + 1)
                .takeWhile(s -> s < 4)
                .filter(i -> i % 2 == 0)
                .map(IterableXTest::compute200Millis)
                .onEach(System.out::println)
                .map(String::trim)
                .toListX();

        assertEquals(2, strings.size());
    }

    @Test
    void testTakeWhileInclusive() {
        final ListX<String> strings = Sequence.generate(1, i -> i + 2)
                .map(String::valueOf)
                .takeWhileInclusive(s -> !s.contains("0"))
                .onEach(System.out::println)
                .map(String::trim)
                .toListX();

        assertEquals(51, strings.size());
    }

    @Test
    void testSkipWhile() {
        final var strings = Sequence.generate(0, i -> i + 1)
                .skipWhile(s -> s < 4)
                .filter(i -> i % 2 == 0)
                .take(6)
                .map(IterableXTest::compute200Millis)
                .onEach(System.out::println)
                .map(String::trim)
                .toListX();

        assertAll(
                () -> assertEquals(6, strings.size()),
                () -> assertEquals("val 4", strings
                        .minBy(s -> Integer.parseInt(s.substring(s.length() - 2).trim()))
                        .orElse(""))
        );
    }

    @Test
    void testRange() {
        assertArrayEquals(
                IntStream.range(5, 10).toArray(),
                Sequence.range(5, 10).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testRangeWithInterval() {
        assertArrayEquals(
                IntStream.range(5, 10).filter(i -> i % 2 == 0).toArray(),
                Sequence.range(5, 10).filter(i -> i % 2 == 0).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testRangeClosed() {
        assertArrayEquals(
                IntStream.rangeClosed(5, 10).toArray(),
                Sequence.rangeClosed(5, 10).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testIterateRandomWithinBound() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5);

        final MapX<Integer, MutableListX<Integer>> group = Sequence
                .generate(integers::random)
                .take(10_000_000)
                .group();

        group.values().forEachOf(List::size, System.out::println);

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> group.values().forEachOf(ListX::isNotEmpty, Assertions::assertTrue)
        );
    }

    @Test
    void testDistinctBy() {
        final var integers = Sequence.of("hallo", "hoe", "is", "het");

        final var strings = integers
                .distinctBy(String::length)
                .map(StringX::of)
                .map(StringX::reversed)
                .toListOf(StringX::toString);

        System.out.println("strings = " + strings);

        assertEquals(List.of("ollah", "eoh", "si"), strings);
    }

    @Test
    void testEmpty() {
        final ListX<Object> listX = Sequence.empty().toListX();
        assertTrue(listX.isEmpty());
    }

}
