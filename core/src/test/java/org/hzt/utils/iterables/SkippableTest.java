package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.utils.collections.ListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkippableTest {

    @Test
    void testSkip() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream()
                .skip(3)
                .toListX();

        final var actual = museumList.skip(3);

        assertEquals(expected, actual);
    }

}
