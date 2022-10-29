package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.utils.collections.ListX;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class SkipableTest {

    @Test
    void testSkip() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = museumList.stream()
                .skip(3)
                .collect(Collectors.toList());

        final ListX<Museum> actual = museumList.skip(3);

        println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

}
