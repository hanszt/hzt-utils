package hzt.collections;

import hzt.function.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class NavigableMapXTest {

    @Test
    void testGetNavigableMap() {
        final SetX<Museum> museumListContainingNulls = SetX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableMap<String, Museum> expected = new TreeMap<>(museumListContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(Museum::getName, It::self)));

        final NavigableMapX<String, Museum> actual = museumListContainingNulls
                .toNavigableMapAssociatedBy(Museum::getName);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

    @Test
    void testGetNavigableMapAssociatedWith() {
        final MutableSetX<Museum> museumSetContainingNulls = MutableSetX
                .of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableMap<Museum, String> expected = new TreeMap<>(museumSetContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(It::self, Museum::getName)));

        final NavigableMapX<Museum, String> actual = museumSetContainingNulls
                .notNullBy(Museum::getName)
                .toNavigableMapAssociatedWith(Museum::getName);

        final Museum firstMuseum = actual.first().getKey();

        System.out.println("firstMuseum = " + firstMuseum);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

}
