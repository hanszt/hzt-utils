package org.hzt.utils.iterables;

import org.hzt.utils.collections.MutableListX;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringableTest {

    @Test
    void joinToStringOfObjectContainingNullWorks() {
        final String string = MutableListX.of(Instant.EPOCH, LocalDate.of(2023, 12, 3), null).joinToString();

        assertEquals("1970-01-01T00:00:00Z, 2023-12-03, null", string);
    }
}