package org.hzt.utils.demo;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.io.FileX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2021SnippetsTest {

    @Test
    void testAoc2021Day1() {
        assertEquals(1748, part2());
    }

    long part2() {
        return FileX.of("input/day1aoc2021.txt").useLines(lines ->
                lines.mapToInt(Integer::parseInt)
                        .windowed(3)
                        .mapToLong(IntList::sum)
                        .zipWithNext((sum, nextSum) -> sum - nextSum)
                        .count(diff -> diff < 0));
    }
}
