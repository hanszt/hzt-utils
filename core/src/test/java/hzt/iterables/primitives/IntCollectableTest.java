package hzt.iterables.primitives;

import hzt.collections.primitives.IntMutableListX;
import hzt.collectors.primitves.IntCollectors;
import hzt.sequences.primitives.IntSequence;
import hzt.tuples.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntCollectableTest {

    @Test
    void testCollect() {
        final IntMutableListX listX = IntSequence.generate(0, i -> ++i)
                .take(1_000)
                .collect(IntMutableListX::empty, IntMutableListX::add);

        assertEquals(1_000, listX.size());
    }

    @Test
    void testTeeing() {
        final Pair<IntListX, IntMutableListX> teeing = IntSequence.generate(0, i -> ++i)
                .take(1_000)
                .teeing(IntCollectors.toList(),
                        IntCollectors.toMutableList(),
                        Pair::of);

        final IntListX integers = teeing.first();
        final IntMutableListX mutableIntList = teeing.second();

        assertAll(
                () -> assertEquals(1_000, integers.size()),
                () -> assertEquals(1_000, mutableIntList.size())
        );
    }
}
