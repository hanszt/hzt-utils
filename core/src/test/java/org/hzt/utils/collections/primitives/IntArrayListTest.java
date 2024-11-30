package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntArrayListTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntArrayListTest.class);

    @Test
    void testNoConcurrentModExceptionInForLoopWhenRemove() {
        final IntMutableList list = new IntArrayList(1, 2, 3, 4, 5, 6);
        for (final int i : list) {
            if (i == 3 || i == 5) {
                list.remove(i);
            }
        }
        LOGGER.atDebug().setMessage(() -> "list = " + list).log();

        assertEquals(IntMutableList.of(1, 2, 4, 6), list);
    }

    @Test
    void testConcurrentModExceptionInForLoopRemove() {
        final var list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        final var exception = assertThrows(ConcurrentModificationException.class, () -> {
                    //noinspection Java8CollectionRemoveIf
                    for (final var integer : list) {
                        if (integer == 3 || integer == 5) {
                            list.remove(integer);
                        }
                    }
                });

        LOGGER.atDebug().setMessage(() -> "list = " + list).setCause(exception).log();
    }

}
