package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.tuples.IndexedValue;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.hzt.utils.gatherers.Gatherers.windowFixed;
import static org.hzt.utils.gatherers.GatherersX.takeWhileIncluding;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionXTest {

    @Test
    void testContainsNot() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        assertTrue(strings.containsNot("strings"));
    }

    @Test
    void testContainsAll() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        final Iterable<String> iterable = Arrays.asList("string", "test");

        assertTrue(strings.containsAll(iterable));
    }

    @Test
    void testDoesNotContainsAll() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        final Iterable<String> iterable = Arrays.asList("string", "test", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testContainsNoneOf() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        final Iterable<String> iterable = Arrays.asList("strings", "testa");

        assertTrue(strings.containsNoneOf(iterable));
    }

    @Test
    void testDoesContainSome() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        final Iterable<String> iterable = Arrays.asList("string", "tesst", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testMapMulti() {
        final ListX<ListX<String>> input = ListX.of(ListX.of("a", "b", "c"), ListX.of("d", "e", "f", "g"));

        final ListX<String> strings = input.mapMulti(Iterable::forEach);

        strings.forEach(It::println);

        assertEquals(ListX.of("a", "b", "c", "d", "e", "f", "g"), strings);
    }

    @Test
    void testShuffled() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        final ListX<Integer> shuffled = integers.shuffled();

        shuffled.forEach(It::println);

        assertTrue(shuffled.containsAll(integers));
    }

    @Test
    void testIfEmpty() {
        final ListX<Integer> list = ListX.empty();
        final MutableSetX<Integer> set2 = MutableSetX.of(1, 2, 3, 4, 5, 6);

        assertAll(
                () -> assertSame(set2, list.ifEmpty(() -> set2)),
                () -> assertIterableEquals(ListX.of(1, 2, 3, 4, 5, 6), set2.ifEmpty(list::toMutableList))
        );
    }

    @Test
    void testCollectionWithIndex() {
        final ListX<Integer> list = ListX.of(1, 2, 4, 8, 16, 32);
        final ListX<IndexedValue<Integer>> indexedValues = list.withIndex();

        assertAll(
                () -> assertEquals(6, list.size()),
                () -> assertEquals(ListX.of(0, 2, 8, 24, 64, 160), indexedValues.map(iv -> iv.index() * iv.value()))
        );
    }

    @Test
    void testFlatMapStreamToIntList() {
        final IntList charInts = ListX.of("hallo", "test").flatMapToInt(s -> s.chars()::iterator);

        assertEquals(IntList.of(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }

    @Nested
    class GathererTests {

        @Test
        void testGatherChunked() {
            final ListX<Integer> input = ListX.of(1, 2, 3, 4);

            final ListX<List<Integer>> windows = input.gather(windowFixed(4));
            final ListX<ListX<Integer>> expected = input.chunked(4);

            assertIterableEquals(expected, windows);
        }
    }

    @Test
    void testTakeWhileInclusive() {
        final Predicate<Integer> isOdd = i -> i % 2 != 0;
        final ListX<Integer> integers = ListX.of(1, 3, 5, 6, 7, 8, 10, 12);

        final ListX<Integer> result = integers.gather(takeWhileIncluding(isOdd));
        final ListX<Integer> takeWhileResult = integers.takeWhile(isOdd);

        assertAll(
                () -> assertEquals(ListX.of(1, 3, 5, 6), result),
                () -> assertEquals(ListX.of(1, 3, 5), takeWhileResult)
        );
    }
}
