package hzt.io;

import hzt.sequences.Sequence;
import hzt.strings.StringX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileXTest {

    @Test
    void testReadLines() {
        final var lines = FileX.of("input/test.txt").readLines();

        assertAll(
                () -> assertEquals("Hello, this is a test, Second line", lines.joinToString()),
                () -> assertEquals(2, lines.size())
        );
    }

    @Test
    void testReadText() {
        final var string = FileX.of("input/test.txt").readText();

        assertEquals("Hello, this is a test\nSecond line", string);
    }

    @Test
    void testReadTextX() {
        final var string = FileX.of("input/test.txt").readTextX();

        assertEquals(StringX.of(String.format("Hello, this is a test%nSecond line")), string);
    }

    @Test
    void useLines() {
        final var string = FileX.of("input/test.txt").useLines(Sequence::joinToString);

        assertEquals("Hello, this is a test, Second line", string);
    }
}
