package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TakeableTest {

    @Test
    void testTake() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream().limit(3).toListX();

        final var actual = museumList.take(3);

        assertEquals(expected, actual);
    }

    @Test
    void testTakeWhile() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = MutableListX.empty();
        for (final var m : museumList) {
            if (!(m.getPaintings().size() < 3)) {
                break;
            }
            expected.add(m);
        }
        final var actual = museumList.takeWhile(museum -> museum.getPaintings().size() < 3);

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

        assertEquals(expected, actual);
    }

    @Test
    void testTakeWhileInclusive() {
        final var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var takeWhileInclusive = list.takeWhileInclusive(i -> i != 5);
        final var takeWhile = list.takeWhile(i -> i != 5);

        assertAll(
                () -> assertEquals(ListX.of(1, 2, 10, 4, 5), takeWhileInclusive),
                () -> assertEquals(ListX.of(1, 2, 10, 4), takeWhile)
        );
    }
}
