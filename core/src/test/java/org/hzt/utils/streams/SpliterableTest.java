package org.hzt.utils.streams;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpliterableTest {

    @Test
    void testAtomicIteratorFromSpliterable() {
        final var spliterable = StreamX.of(List.of(1, 2, 3, 4, 5, 6, 7, 8));

        final var atomicIterator = spliterable.atomicIterator();

        final Set<Integer> set = new HashSet<>();

        //noinspection StatementWithEmptyBody
        while (atomicIterator.tryAdvance(set::add));

        assertEquals(8, set.size());
    }

}
