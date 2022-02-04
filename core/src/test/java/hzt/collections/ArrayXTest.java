package hzt.collections;

import hzt.utils.It;
import hzt.test.Generator;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayXTest {

    @Test
    void testArrayX() {
        ArrayX<Integer> array = ArrayX.of(1, 3, 4, 5, 3);

        assertTrue(array.all(Objects::nonNull));
    }

    @Test
    void testArrayGroupBy() {
        ArrayX<String> array = ArrayX.of("hallo", "raar", "gedoe", "moe", "stom");

        array.forEach(System.out::println);

        final MapX<Integer, MutableListX<String>> group = array.groupBy(String::length);

        System.out.println("group = " + group);

        assertEquals(ListX.of("raar", "stom"), group.get(4));
    }

    @Test
    void testArraySum() {
        ArrayX<Long> array = ArrayX.of(40, Generator::fib);

        array.forEach(It::println);

        final long sum = array.sumOfLongs(It::asLong);

        System.out.println("sum = " + sum);

        assertEquals(165580140, sum);
    }

}
