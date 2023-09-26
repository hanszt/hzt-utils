package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IntArrayListTest {

    @Test
    void testNoConcurrentModExceptionInForLoopWhenRemove() {
        final IntMutableList list = new IntArrayList(1, 2, 3, 4, 5, 6);
        for (final int i : list) {
            if (i == 3 || i == 5) {
                list.remove(i);
            }
        }
        System.out.println("list = " + list);

        assertEquals(IntMutableList.of(1, 2, 4, 6), list);
    }

    @Test
    void testConcurrentModExceptionInForLoopRemove() {
        final var list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        final var exception =
                assertThrows(ConcurrentModificationException.class, () -> {
                    //noinspection Java8CollectionRemoveIf
                    for (final var integer : list) {
                        if (integer == 3 || integer == 5) {
                            list.remove(integer);
                        }
                    }
                });
        exception.printStackTrace();
        System.out.println("list = " + list);
    }

}
