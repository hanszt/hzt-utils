package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TakeableTest {

    @Test
    void testTake() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream().limit(3).toList();

        final IterableX<Museum> actual = museumList.take(3);

        It.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTakeWhile() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = new ArrayList<>();
        for (final var m : museumList) {
            if (!(m.getPaintings().size() < 3)) {
                break;
            }
            expected.add(m);
        }

        final var actual = museumList.takeWhile(museum -> museum.getPaintings().size() < 3);

        It.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTakeLastWhile() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var strings = "De sterrennacht, Het melkmeisje, Les Demoiselles d'Avignon, Meisje met de parel, Le Rêve, Meisje met de rode hoed, Guernica ".split(",");

        final var expected = ListX.of(strings).toSetXOf(String::trim);

        final var actual = museumList
                .flatMap(Museum::getPaintings)
                .takeLastWhile(Painting::isInMuseum)
                .toSetXOf(Painting::name);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTakeWhileInclusive() {
        final var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> takeWhileInclusive = list.takeWhileInclusive(i -> i != 5);
        final var takeWhile = list.takeWhile(i -> i != 5);

        It.println("integers = " + takeWhileInclusive);

        assertAll(
                () -> assertIterableEquals(List.of(1, 2, 10, 4, 5), takeWhileInclusive),
                () -> assertIterableEquals(List.of(1, 2, 10, 4), takeWhile)
        );
    }
}
