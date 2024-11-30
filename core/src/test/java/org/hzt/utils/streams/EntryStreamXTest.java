package org.hzt.utils.streams;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntryStreamXTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntryStreamXTest.class);

    @Test
    void testEntryStreamFromMap() {
        final var map = Map.of(1, "This", 2, "is", 3, "a", 4, "test");

        final var entries = EntryStreamX.ofMap(map)
                .mapByKeys(LocalDate::ofEpochDay)
                .parallel()
                .inverted()
                .toMapX();

        LOGGER.atDebug().setMessage(() -> "entries = " + entries).log();

        assertEquals(Set.of("This", "is", "a", "test"), entries.keySet());
    }

}
