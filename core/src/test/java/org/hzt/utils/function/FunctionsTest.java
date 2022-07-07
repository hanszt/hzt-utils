package org.hzt.utils.function;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hzt.utils.function.Functions.not;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FunctionsTest {

    @Test
    void testBiPredicateNot() {
        final List<Integer> list = Sequence.of(1, 2, 3, 4, 4, 8, 10, 6, 5)
                .zipWithNext()
                .takeWhile(not(Integer::equals))
                .merge()
                .toList();

        System.out.println("list = " + list);

        assertFalse(list.contains(8));
    }
}
