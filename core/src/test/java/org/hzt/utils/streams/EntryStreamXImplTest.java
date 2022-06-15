package org.hzt.utils.streams;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntryStreamXImplTest {

    @Test
    void isInstanceOfStreamXImpl() {
        final var entries = new EntryStreamXImpl<>(Map.of("This", "is", "a", "test").entrySet().stream());
        //noinspection ConstantConditions
        assertTrue(entries instanceof StreamXImpl);
    }

}
