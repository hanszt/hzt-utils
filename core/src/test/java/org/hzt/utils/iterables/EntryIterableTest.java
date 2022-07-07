package org.hzt.utils.iterables;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class EntryIterableTest {

    @Test
    void testEntryIterableAny() {
        final var anySubsequentEqual = Sequence.of(1, 2, 3, 4, 4, 5, 6, 7)
                .zipWithNext()
                .any(Objects::equals);

        assertTrue(anySubsequentEqual);
    }

    @Test
    void testEntryIterableNone() {
        final var noSubsequentEqual = Sequence.of(1, 2, 1, 4, 5, 6, 7, 10)
                .zipWithNext()
                .none(Objects::equals);

        assertTrue(noSubsequentEqual);
    }

    @Test
    void testEntryIterableAll() {
        final var isSorted = Sequence.of(1, 2, 3, 4, 4, 5, 6, 7)
                .zipWithNext()
                .all((first, second) -> first.compareTo(second) <= 0);

        assertTrue(isSorted);
    }

}
