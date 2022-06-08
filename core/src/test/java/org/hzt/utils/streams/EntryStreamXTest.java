package org.hzt.utils.streams;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntryStreamXTest {

    @Test
    void testEntryStreamFromMap() {
        final var map = Map.of(1, "This", 2, "is", 3, "a", 4, "test");

        final var entries = EntryStreamX.ofMap(map)
                .mapKeys(LocalDate::ofEpochDay)
                .parallel()
                .isParallel(System.out::println)
                .inverted()
                .toMapX();

        System.out.println("entries = " + entries);

        assertEquals(Set.of("This", "is", "a", "test"), entries.keySet());
    }

    static class StreamableMap implements EntryStreamX<Integer, String> {

        private final Map<Integer, String> map;

        StreamableMap(Map<Integer, String> map) {
            this.map = map;
        }


        @Override
        public @NotNull Spliterator<Map.Entry<Integer, String>> spliterator() {
            return map.entrySet().spliterator();
        }
    }

}
