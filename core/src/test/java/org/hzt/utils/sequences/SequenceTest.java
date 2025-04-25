package org.hzt.utils.sequences;

import org.hzt.demo.IO;
import org.hzt.utils.function.Function;
import org.hzt.utils.function.Functions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static java.util.Collections.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hzt.demo.function.IntegerFunctions.*;
import static org.hzt.demo.function.StringFunctions.startsWith;
import static org.hzt.demo.function.StringFunctions.toStringLength;
import static org.hzt.utils.function.Comparators.comparing;
import static org.hzt.utils.function.Functions.*;
import static org.hzt.utils.function.Predicate.isEqual;
import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {

    @Test
    void testSequenceEmpty() {
        final Sequence<Object> sequence = Sequence.empty();

        assertThat(sequence.first().isPresent()).isFalse();
        assertTrue(sequence.none());
    }

    @Test
    void testSingleOrNull() {
        assertThat(Sequence.of(1).single().orElseThrow()).isEqualTo(1);
        assertThat(Sequence.empty().single().orElse(null)).isNull();
        assertThat(Sequence.of(1, 2, 3, 4).single().orElse(null)).isNull();
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
        final List<String> list = Arrays.asList("Hallo", "dit", null, "is", "een", "test", null);

        final int result = Sequence.of(list)
                .filter(Functions.<String>isNotNull())
                .map(toStringLength)
                .filter(greaterThan(3))
                .reduce(sum)
                .orElseThrow();

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
                .andThen(SequenceExtensions
                        .<Integer>windowed(10, 2, true)
                        .andThen(IO.<List<Integer>>println()));

        final List<List<Integer>> windows = windowedSequence.toList();

        System.out.println("windows = " + windows);

        assertTrue(windowedSequence.any());
        assertEquals(11, windows.size());
    }

    @Test
    public void testSkipWhileTakeWhile() {
        final List<Integer> list = Sequence.iterate(1, times(2))
                .takeWhile(lessThanEqual(8192))
                .skipWhile(lessThan(8)/*.or(greaterThan(3))*/)
                .toList();

        assertThat(Sequence.of(1, 1, 1).skipWhile(isEqual(1))).isEmpty();
        assertThat(Sequence.of(1, 1, 1).skipWhile(isEqual(2))).hasSize(3);

        assertEquals(Arrays.asList(8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192), list);
    }

    @Test
    public void testMax() {
        final Random random = new Random(0);
        final Function<Integer, Integer> nextRandomBound10 = new Function<Integer, Integer>() {
            public Integer apply(final Integer integer) {
                return random.nextInt(10);
            }
        };
        final int max = Sequence.iterate(0, nextRandomBound10)
                .take(100)
                .reduce(Functions.<Integer>max())
                .orElseThrow();

        assertEquals(9, max);
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
        final Sequence<String> stringSequence = Sequence.of("Hallo", "dit", "is", "een", "test");
        final List<String> sortedByLength = stringSequence
                .sorted(comparing(toStringLength))
                .toList();

        final List<String> reverseSorted = stringSequence
                .sorted(reverseOrder())
                .toList();

        assertEquals(Arrays.asList("is", "dit", "een", "test", "Hallo"), sortedByLength);
        assertEquals(Arrays.asList("test", "is", "een", "dit", "Hallo"), reverseSorted);
    }

    @Test
    void testReverseOf() {
        final String[] input = {"hoi", "liesje", "leerde", "lotje", "lopen"};
        final List<String> list1 = Sequence.reverseOf(input)
                .filter(startsWith("l"))
                .toList();

        final String single = Sequence.of(input)
                .filter(startsWith("l").negate())
                .single()
                .orElseThrow();

        assertEquals(Arrays.asList("lopen", "lotje", "leerde", "liesje"), list1);
        assertEquals("hoi", single);
    }

    @Test
    void testOfNullable() {
        List<String> s = null;

        assertThat(Sequence.ofNullable(s)
                .andThen(SequenceExtensions.<String>flatten())
                .toList()).isEmpty();

        s = Arrays.asList("hallo", "dit", "is", "een", "test");

        assertThat(Sequence.ofNullable(s)
                .andThen(SequenceExtensions.<String>flatten())
                .toList()).hasSize(5);

    }

    @Test
    void testLongSequenceChainOnlyNextWorks() {
        final Iterator<Integer> sums = Sequence.iterate(1, times(2))
                .takeWhile(lessThanEqual(8192))
                .skipWhile(lessThan(8))
                .andThen(SequenceExtensions.<Integer>windowed(2))
                .map(new Function<List<Integer>, Integer>() {

                    public Integer apply(final List<Integer> integers) {
                        return Sequence.reverseOf(integers).reduce(sum).orElse(0);
                    }
                })
                .iterator();

        assertEquals(24, sums.next());
        assertEquals(48, sums.next());
        assertEquals(96, sums.next());

        final List<Integer> list = new Sequence<Integer>() {
            public Iterator<Integer> iterator() {
                return sums;
            }
        }.toList();

        assertThat(list).isEqualTo(Arrays.asList(192, 384, 768, 1536, 3072, 6144, 12288));
    }

}
