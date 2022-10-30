package org.hzt.geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridPoint2DTest {

    @Test
    @DisplayName("Test sum of two GridPoint2D")
    void testSumOfTwoGridPoint2D() {
        var point1 = GridPoint2D.from(1, 2);
        var point2 = GridPoint2D.from(2, 5);

        assertEquals(GridPoint2D.from(3, 7), point1.add(point2));
    }

    @Test
    @DisplayName("Test Point2d scaled")
    void testPoint2DScaled() {
        var point2D = GridPoint2D.from(2, 8);

        assertEquals(GridPoint2D.from(6, 24), point2D.multiply(3));
    }

}
