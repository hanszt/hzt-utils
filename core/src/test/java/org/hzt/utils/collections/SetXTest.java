package org.hzt.utils.collections;

import org.hzt.test.model.Painting;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetXTest {

    @Test
    void testToSetYieldsUnModifiableSet() {
        final var auction = Generator.createVanGoghAuction();
        final var yearToAdd = Year.of(2000);

        final var years = auction.toSetOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void testSetXCanNotBeCastToMutableSetX() {
        final var setX = SetX.of(1, 2, 3, 4, 5, 10);
        //noinspection RedundantClassCall
        assertAll(
                () -> assertThrows(ClassCastException.class, () -> MutableSetX.class.cast(setX)),
                () -> assertEquals(6, setX.size())
        );
    }

    @Nested
    class EqualsTests {

        @Test
        void testListXEquals() {
            final var set1 = SetX.of("This", "is", "a", "test");
            final var set2 = SetX.of("is", "This", "a", "test");

            assertAll(
                    () -> assertEquals(set2, set1),
                    () -> assertEquals(set1, set2)
            );
        }

        @Test
        void testListXAndListDoNotEqual() {
            final var setX = SetX.of("This", "is", "a", "test");
            final var set = Set.of("This", "is", "a", "test");

            assertAll(
                    () -> assertNotEquals(set, setX),
                    () -> assertNotEquals(setX, set)
            );
        }
    }
}
