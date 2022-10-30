package org.hzt.geometry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridPoint3DTest {

    @Test
    @DisplayName("Test add two points 3d")
    void testAddTwoPoints3D() {
        var point1 = GridPoint3D.from(2, 4, 3);
        var point2 = GridPoint3D.from(3, 2, 5);

        assertEquals(GridPoint3D.from(5, 6, 8), point1.add(point2));
    }

    @Test
    @DisplayName("Test cross product")
    void testCrossProduct() {
        var point1 = GridPoint3D.from(2, 4, 3);
        var point2 = GridPoint3D.from(3, 2, 5);

        final var actual = point1.crossProduct(point2);

        assertEquals(GridPoint3D.from(14, -1, -8), actual);
    }

}
