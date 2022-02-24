package hzt.collections;

import hzt.collectors.CollectorsX;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Deque;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.*;

class MutableCollectionXTest {

    @Test
    void testFilterAndMapToArrayDeque() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

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
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final ListX<LocalDate> expectedLocalDates = museumList.stream()
                .map(Museum::getDateOfOpening)
                .collect(CollectorsX.toListX());

        final ListX<LocalDate> actualLocalDates = museumList
                .mapTo(MutableListX::empty, Museum::getDateOfOpening);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

}
