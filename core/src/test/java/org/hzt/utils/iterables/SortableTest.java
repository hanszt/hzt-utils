package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SortableTest {

    @Test
    void testSortedSequenceLazyEvaluation() {
        final AtomicInteger counter = new AtomicInteger();

        final Sequence<Integer> sequence = Sequence.generate(9, i -> --i)
                .take(10)
                .onEach(i -> counter.getAndIncrement())
                .sorted();

        assertEquals(0, counter.get());

        final List<Integer> integers = sequence.toList();

        assertAll(
                () -> assertEquals(10, counter.get()),
                () -> assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), integers)
        );
    }

    @Test
    void testSortedIntSequenceLazyEvaluation() {
        final AtomicInteger counter = new AtomicInteger();

        final IntSequence sequence = IntSequence.generate(9, i -> --i)
                .take(10)
                .onEach(i -> counter.getAndIncrement())
                .sorted();

        assertEquals(0, counter.get());

        final int[] integers = sequence.toArray();

        assertAll(
                () -> assertEquals(10, counter.get()),
                () -> assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, integers)
        );
    }

    @Test
    void testSequenceSortedBy() {
        final List<String> englishNameList = Arrays.asList(
                "Oliver",
                "Harry",
                "George",
                "Noah",
                "Jack",
                "Jacob",
                "Leo",
                "Oscar",
                "Charlie"
        );
        final List<String> names = Sequence.of(englishNameList.stream())
                .take(10)
                .sortedBy(String::length)
                .toList();

        System.out.println("names = " + names);

        final List<String> expectedNames = Arrays.asList("Leo, Noah, Jack, Harry, Jacob, Oscar, Oliver, George, Charlie"
                .split(", "));

        assertEquals(expectedNames, names);
    }

    @Test
    void testSequenceDatesSortedBy() {
        final ListX<LocalDate> localDates = Sequence.generate(LocalDate.parse("2019-10-15"), date -> date.minusWeeks(1))
                .take(10)
                .onEach(System.out::println)
                .sortedBy(LocalDate::getDayOfYear)
                .toListX();

        System.out.println("localDates = " + localDates);

        final ListX<LocalDate> expectedDates = Sequence
                .of("2019-08-13, 2019-08-20, 2019-08-27, 2019-09-03, 2019-09-10, 2019-09-17, 2019-09-24, 2019-10-01, 2019-10-08, 2019-10-15"
                        .split(", "))
                .map(LocalDate::parse)
                .toListX();

        assertEquals(expectedDates, localDates);
    }
}
