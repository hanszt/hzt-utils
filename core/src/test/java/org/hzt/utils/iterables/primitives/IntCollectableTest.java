package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.collectors.primitves.IntCollectors;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntCollectableTest {

    @Test
    void testCollect() {
        final IntMutableList listX = IntSequence.generate(0, i -> ++i)
                .take(1_000)
                .collect(IntMutableList::empty, IntMutableList::add);

        assertEquals(1_000, listX.size());
    }

    @Test
    void testTeeing() {
        final Pair<IntList, IntMutableList> teeing = IntSequence.generate(0, i -> ++i)
                .take(1_000)
                .teeing(IntCollectors.toList(),
                        IntCollectors.toMutableList(),
                        Pair::of);

        final IntList integers = teeing.first();
        final IntMutableList mutableIntList = teeing.second();

        assertAll(
                () -> assertEquals(1_000, integers.size()),
                () -> assertEquals(1_000, mutableIntList.size())
        );
    }
}
