package org.hzt.utils.streams;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.SetX;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Spliterator;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EntryStreamXTest {

    @Test
    void testEntryStreamFromMap() {
        final MapX<Integer, String> map = MapX.of(1, "This", 2, "is", 3, "a", 4, "test");

        final MapX<String, LocalDate> entries = EntryStreamX.of(map)
                .mapByKeys(LocalDate::ofEpochDay)
                .parallel()
                .isParallel(System.out::println)
                .inverted()
                .toMapX();

        System.out.println("entries = " + entries);

        assertTrue(entries.keySet().containsAll(SetX.of("This", "is", "a", "test")));
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
