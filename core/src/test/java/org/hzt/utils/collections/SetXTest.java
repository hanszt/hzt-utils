package org.hzt.utils.collections;

import org.hzt.test.model.Painting;
import org.hzt.utils.test.Generator;
import org.hzt.utils.test.model.PaintingAuction;
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
        final PaintingAuction auction = Generator.createVanGoghAuction();
        final Year yearToAdd = Year.of(2000);

        final Set<Year> years = auction.toSetOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void testSetXCanNotBeCastToMutableSetX() {
        final SetX<Integer> setX = SetX.of(1, 2, 3, 4, 5, 10);
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
            final SetX<String> set1 = SetX.of("This", "is", "a", "test");
            final SetX<String> set2 = SetX.of("is", "This", "a", "test");

            assertAll(
                    () -> assertEquals(set2, set1),
                    () -> assertEquals(set1, set2)
            );
        }

        @Test
        void testListXAndListDoNotEqual() {
            final SetX<String> setX = SetX.of("This", "is", "a", "test");
            final Set<String> set = SetX.of("This", "is", "a", "test").toSet();

            assertAll(
                    () -> assertNotEquals(set, setX),
                    () -> assertNotEquals(setX, set)
            );
        }
    }
}
