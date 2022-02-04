package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionViewTest {

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

}
