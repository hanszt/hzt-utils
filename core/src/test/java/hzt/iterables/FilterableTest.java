package hzt.iterables;

import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FilterableTest {

    @Test
    void testFilterNotTo() {
        final LinkedBlockingDeque<Integer> integers = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filterNotTo(LinkedBlockingDeque::new, IntX::isEven);

        integers.forEach(It::println);

        integers.forEach(i -> assertTrue(IntX.isOdd(i)));
    }
}
