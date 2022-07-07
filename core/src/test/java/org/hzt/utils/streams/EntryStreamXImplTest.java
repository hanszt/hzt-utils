package org.hzt.utils.streams;

import org.hzt.utils.collections.MapX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryStreamXImplTest {

    @Test
    void isInstanceOfStreamXImpl() {
        final EntryStreamXImpl<String, String> entries = new EntryStreamXImpl<>(MapX.of("This", "is", "a", "test").entrySet().stream());
        //noinspection ConstantConditions
        assertTrue(entries instanceof StreamXImpl);
    }

}
