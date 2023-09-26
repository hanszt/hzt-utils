package org.hzt.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Grid2DUtilsTest {

    @Test
    void testMapGrid() {
        final var grid = new Integer[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };

        final var listGrid = Grid2DUtils.toListGrid(grid);

        final var strings = Grid2DUtils.mapGrid(listGrid, String::valueOf);

        System.out.println("AocUtils.gridAsString(strings) = " + Grid2DUtils.gridAsString(strings));

        assertEquals(2, strings.size());
    }

    @Test
    void testSwapInListGrid() {
        final var grid = new Integer[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };

        final var listGrid = Grid2DUtils.toListGrid(grid);

        final var strings = Grid2DUtils.swap(listGrid, 1, 2, 0, 3);

        System.out.println("AocUtils.gridAsString(strings) = " + Grid2DUtils.gridAsString(strings));

        assertAll(
                () -> assertEquals(grid[1][2], listGrid.get(0).get(3)),
                () -> assertEquals(grid[0][3], listGrid.get(1).get(2))
        );
    }

    @Test
    void testToIntGrid() {
        final var grid = new Integer[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8}
        };

        final var listGrid = Grid2DUtils.toListGrid(grid);

        final var ints = Grid2DUtils.toIntGrid(listGrid, i -> i);

        assertEquals(ints[1][3], grid[1][3]);
    }

}
