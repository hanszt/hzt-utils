package org.hzt.utils.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class MutableCollectionXTest {

    @Test
    void testFilterAndMapToArrayDeque() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Deque<LocalDate> expectedLocalDates = museumList.stream()
                .filter(museum -> museum.getPaintings().size() > 3)
                .map(Museum::getDateOfOpening)
                .collect(toCollection(ArrayDeque::new));

        final Deque<LocalDate> actualLocalDates = museumList
                .filterBy(Museum::getPaintings, paintings -> paintings.size() > 3)
                .mapTo(ArrayDeque::new, Museum::getDateOfOpening);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

    @Test
    void testMapToListX() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedLocalDates = museumList.stream()
                .map(Museum::getDateOfOpening)
                .collect(Collectors.toList());

        final ListX<LocalDate> actualLocalDates = museumList
                .mapTo(MutableListX::empty, Museum::getDateOfOpening);

        System.out.println("actualLocalDates = " + actualLocalDates);
        System.out.println("expectedLocalDates = " + expectedLocalDates);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

}
