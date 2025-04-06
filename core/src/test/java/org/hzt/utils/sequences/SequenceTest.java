package org.hzt.utils.sequences;

import org.hzt.utils.function.Function;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hzt.demo.function.IntegerFunctions.*;
import static org.hzt.demo.function.StringFunctions.plusStringLength;
import static org.hzt.demo.function.StringFunctions.stringLength;
import static org.hzt.utils.function.Comparators.comparing;
import static org.hzt.utils.function.Functions.greaterThan;
import static org.hzt.utils.sequences.SequenceExtensions.scan;
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
    public void testWindowed() {
        final List<List<Integer>> windows = Sequence.iterate(1, plus(1))
                .take(6)
                .andThen(SequenceExtensions.<Integer>windowed(3))
                .toList();

        assertEquals(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(2, 3, 4),
                Arrays.asList(3, 4, 5),
                Arrays.asList(4, 5, 6)
        ), windows);
    }

    @Test
    public void testWindowedCustom() {
        final List<List<Integer>> windows = Sequence
                .iterate(1, plus(2).andThen(mod(20)))
                .take(22)
                .andThen(SequenceExtensions.<Integer>windowed(10, 2, true))
//                .andThen(IO.<List<Integer>>println())
                .toList();

        System.out.println("windows = " + windows);

        assertEquals(11, windows.size());
    }

    @Test
    public void testScan() {
        final List<Integer> list = Sequence.of("Some", "new", "text")
                .andThen(scan(0, plusStringLength))
                .toList();

        assertEquals(Arrays.asList(0, 4, 7, 11), list);
    }

    @Test
    public void testSorted() {
        final List<String> strings = Sequence.of("Hallo", "dit", "is", "een", "test")
                .sorted(comparing(stringLength))
                .toList();

        assertEquals(Arrays.asList("is", "dit", "een", "test", "Hallo"), strings);
    }
}
