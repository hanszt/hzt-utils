package org.hzt.utils.sequences;

import org.hzt.utils.function.BiFunction;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SequenceExtensionsTest {

    @Test
    void testZip() {
        final List<Integer> list = Sequence.of("Some", "new", "text")
                .andThen(SequenceExtensions.zip(Arrays.asList("a", "b", "c", "d"), new BiFunction<String, String, Integer>() {
                    public Integer apply(final String s1, final String s2) {
                        return s1.length() + s2.length();
                    }
                }))
                .toList();

        assertEquals(Arrays.asList(5, 4, 5), list);
    }

}