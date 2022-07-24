package org.hzt.utils.streams;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntryStreamXTest {

    @Test
    void testEntryStreamFromMap() {
        final var map = Map.of(1, "This", 2, "is", 3, "a", 4, "test");

        final var entries = EntryStreamX.ofMap(map)
                .mapByKeys(LocalDate::ofEpochDay)
                .parallel()
                .isParallel(System.out::println)
                .inverted()
                .toMapX();

        System.out.println("entries = " + entries);

        assertEquals(Set.of("This", "is", "a", "test"), entries.keySet());
    }

}
