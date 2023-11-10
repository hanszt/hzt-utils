package org.hzt.utils.streams;

import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpliterableTest {

    @Test
    void testAtomicIteratorFromSpliterable() {
        final StreamX<Integer> spliterable = StreamX.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        final AtomicIterator<Integer> atomicIterator = spliterable.atomicIterator();

        final Set<Integer> set = new HashSet<>();

        //noinspection StatementWithEmptyBody
        while (atomicIterator.tryAdvance(set::add)) {
        }

        assertEquals(8, set.size());
    }

}
