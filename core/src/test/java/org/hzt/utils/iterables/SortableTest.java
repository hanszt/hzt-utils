package org.hzt.utils.iterables;

import org.hzt.test.model.Person;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.reverseOrder;
import static org.hzt.utils.Patterns.blankStringPattern;
import static org.hzt.utils.Patterns.splitToSequence;
import static org.junit.jupiter.api.Assertions.*;

class SortableTest {

    @Test
    void testSortedSequenceLazyEvaluation() {
        final var counter = new AtomicInteger();

        final var sequence = Sequence.iterate(9, i -> --i)
                .take(10)
                .onEach(i -> counter.getAndIncrement())
                .sorted();

        assertEquals(0, counter.get());

        final var integers = sequence.toList();

        assertAll(
                () -> assertEquals(10, counter.get()),
                () -> assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), integers)
        );
    }

    @Test
    void testSortedIntSequenceLazyEvaluation() {
        final var counter = new AtomicInteger();

        final var sequence = IntSequence.iterate(9, i -> --i)
                .take(10)
                .onEach(i -> counter.getAndIncrement())
                .sorted();

        assertEquals(0, counter.get());

        final var integers = sequence.toArray();

        assertAll(
                () -> assertEquals(10, counter.get()),
                () -> assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, integers)
        );
    }

    @Test
    void testSequenceSortedBy() {
        final var englishNameList = List.of(
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
        final var names = Sequence.of(englishNameList)
                .take(10)
                .sortedBy(String::length)
                .toList();

        System.out.println("names = " + names);

        final var expectedNames = List.of("Leo, Noah, Jack, Harry, Jacob, Oscar, Oliver, George, Charlie"
                .split(", "));

        assertEquals(expectedNames, names);
    }

    @Test
    void testSequenceDatesSortedBy() {
        final var localDates = Sequence.iterate(LocalDate.parse("2019-10-15"), date -> date.minusWeeks(1))
                .take(10)
                .onEach(System.out::println)
                .sortedBy(LocalDate::getDayOfYear)
                .toList();

        System.out.println("localDates = " + localDates);

        final var expectedDates = Sequence
                .of("2019-08-13, 2019-08-20, 2019-08-27, 2019-09-03, 2019-09-10, 2019-09-17, 2019-09-24, 2019-10-01, 2019-10-08, 2019-10-15"
                        .split(", "))
                .map(LocalDate::parse)
                .toList();

        assertEquals(expectedDates, localDates);
    }

    @Test
    void isSortedInReverseOrder() {
        assertFalse(Sequence.of(1, 2, 3, 3, 4, 5, 6, 7, 8).isSorted(reverseOrder()));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Hans Huib  Sophie Ted",
            "Dominic Hans Marlon Marnix  Rashied",
            "Charlotte Hans Joop Marjolein  Matthijs Thom",
            "Adi Hans Judith Koen Pauline  Ted"
    })
    void isSortedInNaturalOrder(String string) {
        final var people = blankStringPattern.splitAsStream(string)
                .map(Person::new)
                .collect(CollectorsX.toListX());

        assertTrue(people.isSortedBy(Person::getName));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Sophie Huib Hans Ted",
            "Dominic Marlon Hans Marnix  Rashied",
            "Charlotte Hans Marjolein Joop  Matthijs Thom",
            "Adi Judith Hans Koen Pauline  Ted"
    })
    void isNotSortedInNaturalOrder(String string) {
        final var people = splitToSequence(blankStringPattern, string)
                .map(Person::new);

        assertFalse(people.isSortedBy(Person::getName));
    }
}
