package org.hzt.utils.sequences;

import org.hzt.utils.function.Function;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hzt.utils.function.Functions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class SequenceTest {

    @Test
    void testSimpleStreamWithMapYieldsIteratorWithNext() {
        final List<String> list = Arrays.asList("Hallo", "dit", "is", "een", "test");

        final Sequence<Integer> sequence = Sequence.of(list)
                .map(new Function<String, Integer>() {

                    public Integer apply(final String s) {
                        fail("Should only be called when consumed with terminal operation");
                        return s.length();
                    }
                });

        assertThat(sequence.iterator()).hasNext();
    }

    @Test
    void testFilterReduce() {
        final List<String> list = Arrays.asList("Hallo", "dit", "is", "een", "test");
        final int result = Sequence.of(list)
                .map(stringLength)
                .filter(greaterThan(3))
                .reduceOrNull(sum);

        assertEquals(9, result);
    }

    @Test
    public void test() {
        final List<List<Integer>> windows = Sequence.iterate(1, plus(2).andThen(mod(20)))
                .take(22)
                .then(SequenceExtensions.<Integer>windowed(10, 2, true))
                .toList();

        System.out.println("windows = " + windows);

        assertEquals(11, windows.size());
    }

    private static final Function<String, Integer> stringLength = new Function<String, Integer>() {
        public Integer apply(final String s) {
            return s.length();
        }
    };
}
