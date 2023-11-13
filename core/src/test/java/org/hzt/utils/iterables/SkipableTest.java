package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.utils.collections.ListX;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.collectors.CollectorsX.toListX;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SkipableTest {

    @Test
    void testSkip() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream()
                .skip(3)
                .collect(toListX());

        final var actual = museumList.skip(3);

        assertEquals(expected, actual);
    }

}
