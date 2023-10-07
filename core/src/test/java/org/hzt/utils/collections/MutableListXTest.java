package org.hzt.utils.collections;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MutableListXTest {

    @Test
    void testMutableListRemoveFirst() {
        final var integers = MutableListX.of(1, 2, 3, 4, 5);

        final var removed = integers.removeFirst();
        final var removed2 = integers.removeFirst();

        assertAll(
                () -> assertEquals(1, removed),
                () -> assertEquals(2, removed2),
                () -> assertEquals(List.of(3, 4, 5), integers)
        );
    }

    @Test
    void testMutableListRemoveLast() {
        final var integers = MutableListX.of(1, 2, 3, 4, 5);

        final var removed = integers.removeLast();

        assertAll(
                () -> assertEquals(5, removed),
                () -> assertEquals(List.of(1, 2, 3, 4), integers)
        );
    }

    @Nested
    class EqualsTests {

        @Test
        void testMutableListXAndListEquals() {
            final var listX = MutableListX.of("This", "is", "a", "test");
            final var list = List.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(list, listX),
                    () -> assertEquals(listX, list)
            );
        }

        @Test
        void whenMutableListXAndListHaveDifferentContentTheyDoNotEqual() {
            final var listX = MutableListX.of("is", "This", "a", "test");
            final var list = List.of("This", "is", "a", "test");

            assertAll(
                    () -> assertNotEquals(list, listX),
                    () -> assertNotEquals(listX, list)
            );
        }

        @Test
        void testMutableListXAndListXEquals() {
            final var listX = MutableListX.of("This", "is", "a", "test");
            final var list = ListX.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(list, listX),
                    () -> assertEquals(listX, list)
            );
        }

        @Test
        void whenMutableListXAndListXHaveDifferentContentTheyDoNotEqual() {
            final var listX = MutableListX.of("is", "This", "a", "test");
            final var list = ListX.of("This", "is", "a", "test");

            assertAll(
                    () -> assertNotEquals(list, listX),
                    () -> assertNotEquals(listX, list)
            );
        }
    }
}
