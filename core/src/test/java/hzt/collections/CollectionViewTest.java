package hzt.collections;

import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionViewTest {

    @Test
    void testContainsNot() {
        final var strings = ListX.of("hallo", "asffasf", "string", "test");

        assertTrue(strings.containsNot("strings"));
    }

    @Test
    void testContainsAll() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = Arrays.asList("string", "test");

        assertTrue(strings.containsAll(iterable));
    }

    @Test
    void testDoesNotContainsAll() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = Arrays.asList("string", "test", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testContainsNoneOf() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = Arrays.asList("strings", "testa");

        assertTrue(strings.containsNoneOf(iterable));
    }

    @Test
    void testDoesContainSome() {
        final ListX<String> strings = ListX.of("hallo", "asffasf", "string", "test");

        Iterable<String> iterable = Arrays.asList("string", "tesst", "not");

        assertFalse(strings.containsAll(iterable));
    }

    @Test
    void testMapMulti() {
        final ListX<ListX<String>> input = ListX.of(ListX.of("a", "b", "c"), ListX.of("d", "e", "f", "g"));

        final ListX<String> strings = input.mapMulti(Iterable::forEach);

        strings.forEach(It::println);

        assertEquals(ListX.of("a", "b", "c", "d", "e", "f", "g"), strings);
    }

}
