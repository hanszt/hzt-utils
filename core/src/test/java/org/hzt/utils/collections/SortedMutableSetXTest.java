package org.hzt.utils.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class SortedMutableSetXTest {

    @Test
    void testGetNavigableSet() {
        final var museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumListContainingNulls.stream()
                .map(Museum::getName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(toCollection(TreeSet::new));

        final var names = museumListContainingNulls.toSortedSetOf(Museum::getName);

        final MutableListX<Integer> list = MutableListX.empty();

        final var average = names
                .onEach(String::length, It
                        .<Consumer<Integer>>self(It::println)
                        .andThen(list::add))
                .filterBy(String::length, length -> length > 14)
                .averageOf(String::length);

        assertAll(
                () -> assertEquals(15.0, average, 1e-9),
                () -> assertEquals(MutableListX.of(14, 15, 14), list),
                () -> assertIterableEquals(names, expected)
        );
    }

    @Test
    void testToSetSortedBy() {
        final var museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableSet<Museum> expected = museumListContainingNulls.stream()
                .filter(i -> i != null && i.getName() != null)
                .sorted(comparing(Museum::getName))
                .collect(toCollection(TreeSet::new));

        final var sortedMuseums = museumListContainingNulls.toSortedSet(Museum::getName);

        assertIterableEquals(sortedMuseums, expected);
    }
}
