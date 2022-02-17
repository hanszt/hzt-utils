package hzt.collections;

import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionViewTest {

    @Test
    void testContainsNot() {
        final var strings = ListView.of("hallo", "asffasf", "string", "test");

        assertTrue(strings.containsNot("strings"));
    }

    @Test
    void testContainsAll() {
        final var strings = ListView.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("string", "test");

        assertTrue(strings.containsAll(iterable));
    }

    @Test
    void testDoesNotContainsAll() {
        final var strings = ListView.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("string", "test", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testContainsNoneOf() {
        final var strings = ListView.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("strings", "testa");

        assertTrue(strings.containsNoneOf(iterable));
    }

    @Test
    void testDoesContainSome() {
        final var strings = ListView.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = List.of("string", "tesst", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testMapMulti() {
        final var input = ListView.of(ListView.of("a", "b", "c"), ListView.of("d", "e", "f", "g"));

        final var strings = input.<String>mapMulti(Iterable::forEach);

        strings.forEach(It::println);

        assertEquals(ListView.of("a", "b", "c", "d", "e", "f", "g"), strings);
    }

    @Test
    void testShuffled() {
        final var integers = ListView.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        final var shuffled = integers.shuffled();

        shuffled.forEach(It::println);

        assertTrue(shuffled.containsAll(integers));
    }

}
