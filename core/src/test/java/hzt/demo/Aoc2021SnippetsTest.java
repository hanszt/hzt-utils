package hzt.demo;

import hzt.io.FileX;
import hzt.strings.StringX;
import hzt.utils.It;
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
                        .boxed()
                        .windowed(3)
                        .map(l -> l.sumOfInts(It::asInt))
                        .zipWithNext()
                        .count((x, y) -> x < y));
    }
}
