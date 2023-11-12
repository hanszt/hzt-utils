package org.hzt.utils.io;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileXTest {

    @Test
    void testReadLines() {
        final ListX<String> lines = FileX.of("input/test.txt").readLines();

        assertAll(
                () -> assertEquals("Hello, this is a test, Second line", lines.joinToString()),
                () -> assertEquals(2, lines.size())
        );
    }

    @Test
    void testReadText() {
        final String string = FileX.of("input/test.txt").readText();

        assertEquals("Hello, this is a test\nSecond line", string);
    }

    @Test
    void testReadTextX() {
        final StringX string = FileX.of("input/test.txt").readTextX();

        assertEquals(StringX.of(String.format("Hello, this is a test%nSecond line")), string);
    }

    @Test
    void useLines() {
        final String string = FileX.of("input/test.txt").useLines(Sequence::joinToString);

        assertEquals("Hello, this is a test, Second line", string);
    }

    @Test
    void testGridInFileTo2DIntArray() {
        final int[][] grid = FileX.of("input/grid.txt").useLines(sequence ->
                sequence.map(StringX::of)
                        .map(line -> line.split(" ").toIntArray(Integer::parseInt))
                .toTypedArray(int[][]::new));

        Arrays.stream(grid).map(Arrays::toString).forEach(It::println);

        assertArrayEquals(new int[] {1, 1, 0, 0, 0, 0, 0}, grid[0]);
    }

    @Test
    void testReadFromResourceFile() {
        final long result = FileX.fromResource("/testfiles/day1aoc2021.txt").useLines(lines ->
                lines.mapToInt(Integer::parseInt)
                        .windowed(3)
                        .mapToLong(IntList::sum)
                        .zipWithNext((sum, nextSum) -> sum - nextSum)
                        .count(diff -> diff < 0));

        assertEquals(1748, result);
    }
}
