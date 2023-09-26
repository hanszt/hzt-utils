package org.hzt.utils.collections;

import org.hzt.test.model.Painting;
import org.hzt.utils.test.Generator;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
