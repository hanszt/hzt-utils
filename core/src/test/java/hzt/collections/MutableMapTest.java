package hzt.collections;

import hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableMapTest {


    @Test
    void testComputeIfPresent() {
        final MutableMap<String, Integer> map = MutableMap.of("1", 1, "2", 2);

        final Integer result = map.computeIfPresent("1", (k, v) -> Integer.parseInt(k) + v);

        map.forEach(It::println);

        assertEquals(2, result);
    }
}
