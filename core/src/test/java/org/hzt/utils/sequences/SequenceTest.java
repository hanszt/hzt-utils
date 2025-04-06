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
import static org.hzt.utils.function.Functions.*;
import static org.hzt.utils.sequences.SequenceExtensions.scan;
import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {

    @Test
    void testSequenceEmpty() {
        final Sequence<Object> sequence = Sequence.empty();

        assertThat(sequence.firstOrNull()).isNull();
        assertTrue(sequence.none());
    }

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
        final Sequence<List<Integer>> windowedSequence = Sequence
                .iterate(1, plus(2).andThen(mod(20)))
                .take(22)
                .andThen(SequenceExtensions.<Integer>windowed(10, 2, true));

        final List<List<Integer>> windows = windowedSequence
//                .andThen(IO.<List<Integer>>println())
                .toList();

        System.out.println("windows = " + windows);

        assertTrue(windowedSequence.any());
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
    public void testSkipWhileTakeWhile() {
        final List<Integer> list = Sequence.iterate(1, times(2))
                .skipWhile(lessThan(8))
                .takeWhile(lessThanEqual(8192))
                .toList();

        assertEquals(Arrays.asList(8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192), list);
    }

    @Test
    public void testDistinct() {
        final List<Integer> list = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 1, 2, 11)
                .distinct()
                .toList();

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), list);
    }


    @Test
    public void testSorted() {
        final List<String> strings = Sequence.of("Hallo", "dit", "is", "een", "test")
                .sorted(comparing(stringLength))
                .toList();

        assertEquals(Arrays.asList("is", "dit", "een", "test", "Hallo"), strings);
    }
}
