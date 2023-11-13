package org.hzt.utils.collections;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BagTest {

    @Test
    void testAddSizeContains() {
        final Bag<String> bag = Bag.empty();

        Sequence.iterate(0, i -> i + 1)
                .map(String::valueOf)
                .take(20)
                .onSequence(s -> s.chunked(4).forEach(System.out::println))
                .forEach(bag::add);

        bag.chunked(4).forEach(System.out::println);

        assertAll(
                () -> assertEquals(20, bag.size()),
                () -> assertTrue(bag.contains("10"))
        );
    }

}