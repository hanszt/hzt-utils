package hzt.iterables;

import hzt.collections.ListView;
import hzt.collections.SetView;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TakeableTest {

    @Test
    void testTake() {
        final ListView<Museum> museumList = ListView.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = museumList.stream().limit(3).collect(Collectors.toList());

        final IterableX<Museum> actual = museumList.take(3);

        It.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTakeWhile() {
        final ListView<Museum> museumList = ListView.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = new ArrayList<>();
        for (Museum m : museumList) {
            if (!(m.getPaintings().size() < 3)) {
                break;
            }
            expected.add(m);
        }

        final ListView<Museum> actual = museumList.takeWhile(museum -> museum.getPaintings().size() < 3);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTakeLastWhile() {
        final ListView<Museum> museumList = ListView.of(TestSampleGenerator.getMuseumListContainingNulls());

        final String[] strings = "De sterrennacht, Het melkmeisje, Les Demoiselles d'Avignon, Meisje met de parel, Le Rêve, Meisje met de rode hoed, Guernica ".split(",");

        final SetView<String> expected = ListView.of(strings).toSetViewOf(String::trim);

        final SetView<String> actual = museumList
                .flatMap(Museum::getPaintings)
                .takeLastWhile(Painting::isInMuseum)
                .toSetViewOf(Painting::name);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTakeWhileInclusive() {
        ListView<Integer> list = ListView.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> takeWhileInclusive = list.takeWhileInclusive(i -> i != 5);
        final ListView<Integer> takeWhile = list.takeWhile(i -> i != 5);

        It.println("integers = " + takeWhileInclusive);

        assertAll(
                () -> assertIterableEquals(Arrays.asList(1, 2, 10, 4, 5), takeWhileInclusive),
                () -> assertIterableEquals(Arrays.asList(1, 2, 10, 4), takeWhile)
        );
    }
}
