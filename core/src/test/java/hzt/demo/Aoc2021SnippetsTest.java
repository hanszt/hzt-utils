package hzt.demo;

import hzt.collections.primitives.IntListX;
import hzt.io.FileX;
import hzt.strings.StringX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Aoc2021SnippetsTest {

    @Test
    void testAoc2021Day1() {
        assertEquals(1748, part2());
    }

    long part2() {
        return FileX.of("input/day1aoc2021.txt").useLines(lines ->
                lines.mapToInt(StringX::toInt)
                        .windowed(3)
                        .map(IntListX::sum)
                        .zipWithNext()
                        .count((x, y) -> x < y));
    }
}
