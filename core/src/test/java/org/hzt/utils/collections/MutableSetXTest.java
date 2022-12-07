package org.hzt.utils.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableSetXTest {

    @Test
    void testIntersect() {
        final MutableSetX<Integer> integers = MutableSetX.of(1, 2, 3, 4, 5, 7);
        final List<Integer> otherInts = List.of(1, 4, 5, 6);

        final SetX<Integer> intersect = integers.intersect(otherInts);

        assertEquals(MutableSetX.of(1, 4, 5), intersect);
    }

    @Test
    void testUnion() {
        MutableSetX<Integer> set = MutableSetX.of(1, 2, 10, 4, 5, 6, 3);

        final MutableSetX<Integer> union = set.union(List.of(2, 3, 4, 5, 7));

        assertEquals(MutableSetX.of(1, 2, 3, 4, 5, 6, 7, 10), union);
    }

}
