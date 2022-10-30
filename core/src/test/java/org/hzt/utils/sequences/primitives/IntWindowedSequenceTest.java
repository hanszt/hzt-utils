package org.hzt.utils.sequences.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class IntWindowedSequenceTest {

    @Test
    void testLargeWindowedSequence() {
        final var windows = IntRange.of(0, 1_000_000)
                .windowed(2_001, 23, true)
                .toListX();

        final var lastWindow = windows.last();

        final var tail = windows.tailFrom(windows.size() - 2);

        It.println("tail = " + tail);

        assertAll(
                () -> assertEquals(43479, windows.size()),
                () -> assertEquals(999_994, lastWindow.first()),
                () -> assertEquals(999_999, lastWindow.last())
        );
    }

    @Test
    void testLargeVariableWindowedSequence() {
        final var windows = IntRange.of(0, 2_000_000)
                .windowed(2_000, size -> --size, 1, step -> ++step, true)
                .toListX();

        final var lastWindow = windows.last();

        final var firstWindow = windows.first();

        final var head = windows.headTo(5);

        head.forEach(It::println);

        final var tail = windows.tailFrom(windows.size() - 5);

        tail.forEach(It::println);

        assertAll(
                () -> assertEquals(2000, windows.size()),
                () -> assertEquals(2000, firstWindow.size()),
                () -> assertEquals(1, lastWindow.size()),
                () -> assertEquals(1999000, lastWindow.single())
        );
    }

    @Test
    void testChunkedVariableWindowSize() {
        var chunkSize = new AtomicInteger();

        final var sizes = IntRange.of(0, 1000)
                .chunked(chunkSize::incrementAndGet)
                .onEach(It::println)
                .mapToInt(IntList::size)
                .toArray();

        assertEquals(45, sizes.length);
    }
}
