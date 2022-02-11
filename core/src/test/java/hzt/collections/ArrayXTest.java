package hzt.collections;

import hzt.sequences.Sequence;
import hzt.test.Generator;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayXTest {

    @Test
    void testArrayX() {
        ArrayX<Integer> array = ArrayX.of(1, 3, 4, 5, 3);

        assertTrue(array.all(Objects::nonNull));
    }

    @Test
    void testArrayXOfNulls() {
        ArrayX<Integer> array = ArrayX.ofNulls(3);

        assertAll(
                () -> assertTrue(array.all(Objects::isNull)),
                () -> assertEquals(3, array.size())
        );
    }

    @Test
    void testArrayGroupBy() {
        ArrayX<String> array = ArrayX.of("hallo", "raar", "gedoe", "moe", "stom");

        array.forEach(It::println);

        final MapX<Integer, MutableListX<String>> group = array.groupBy(String::length);

        It.println("group = " + group);

        assertEquals(ListX.of("raar", "stom"), group.get(4));
    }

    @Test
    void testArraySum() {
        ArrayX<Long> array = ArrayX.of(40, Generator::fib);

        array.forEach(It::println);

        final long sum = array.sumOfLongs(It::asLong);

        It.println("sum = " + sum);

        assertEquals(165580140, sum);
    }

    @Test
    void testBinarySearchFrom() {
        ArrayX<Long> array = ArrayX.of(40, Generator::fib);

        array.forEach(It::println);

        // the inverted insertion point (-insertion point - 1)
        final long index = array.binarySearch(10, val -> val.compareTo(34L));

        assertEquals(-11, index);
    }

    @Test
    void mapToArrayX() {
        final var arrayX = Sequence.generate(0L, n -> ++n)
                .map(Generator::fib)
                .take(40)
                .toArrayX(Long[]::new);

        arrayX.forEach(It::println);

        assertTrue(arrayX.isNotEmpty());
    }

}
