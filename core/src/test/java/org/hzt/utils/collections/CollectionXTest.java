package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        final MutableSetX<Integer> set2 = MutableSetX.of(1, 2, 3, 4, 5, 6);

        assertAll(
                () -> assertSame(set2, list.ifEmpty(() -> set2)),
                () -> assertEquals(ListX.of(1, 2, 3, 4, 5, 6), set2.ifEmpty(list::toMutableList))
        );
    }

}
