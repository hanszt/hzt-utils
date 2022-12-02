package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.IntList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionXTest {

    @Test
    void testContainsNot() {
        final var strings = ListX.of("hallo", "asffasf", "string", "test");

        assertTrue(strings.containsNot("strings"));
    }

    @Test
    void testContainsAll() {
        final var strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("string", "test");

        assertTrue(strings.containsAll(iterable));
    }

    @Test
    void testDoesNotContainsAll() {
        final var strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("string", "test", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testContainsNoneOf() {
        final var strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("strings", "testa");

        assertTrue(strings.containsNoneOf(iterable));
    }

    @Test
    void testDoesContainSome() {
        final var strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("string", "tesst", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testMapMulti() {
        final var input = ListX.of(ListX.of("a", "b", "c"), ListX.of("d", "e", "f", "g"));

        final var strings = input.<String>mapMulti(Iterable::forEach);

        strings.forEach(It::println);

        assertEquals(ListX.of("a", "b", "c", "d", "e", "f", "g"), strings);
    }

    @Test
    void testShuffled() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        final var shuffled = integers.shuffled();

        shuffled.forEach(It::println);

        assertTrue(shuffled.containsAll(integers));
    }

    @Test
    void testIfEmpty() {
        final var list = ListX.<Integer>empty();
        final var set2 = MutableSetX.of(1, 2, 3, 4, 5, 6);

        assertAll(
                () -> assertSame(set2, list.ifEmpty(() -> set2)),
                () -> assertIterableEquals(ListX.of(1, 2, 3, 4, 5, 6), set2.ifEmpty(list::toMutableList))
        );
    }

    @Test
    void testCollectionWithIndex() {
        final var list = ListX.of(1, 2, 4, 8, 16, 32);
        final var indexedValues = list.withIndex();

        assertAll(
                () -> assertEquals(6, list.size()),
                () -> assertEquals(ListX.of(0, 2, 8, 24, 64, 160), indexedValues.map(iv -> iv.index() * iv.value()))
        );
    }

    @Test
    void testFlatMapStreamToIntList() {
        final var charInts = ListX.of("hallo", "test").flatMapToInt(s -> s.chars()::iterator);

        assertEquals(IntList.of(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }

}
