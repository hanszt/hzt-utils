package hzt.utils;

import hzt.collections.MutableListX;
import hzt.strings.StringX;
import hzt.test.Generator;
import hzt.test.model.PaintingAuction;
import hzt.tuples.Pair;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformableTest {

    @Test
    void testCreateATransformableOfSomethingAndUseTheDefaultFunctions() {
        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();
        final var nijntje = Painting.of("Nijntje");

        final LocalDate localDate = Transformable.from(vanGoghAuction)
                .apply(a -> a.setMostPopularPainting(nijntje))
                .let(PaintingAuction::getDateOfOpening);

        assertAll(
                () -> assertEquals(2, localDate.getDayOfMonth()),
                () -> assertEquals(nijntje, vanGoghAuction.getMostPopularPainting())
        );
    }

    @Test
    void testAlsoWhen() {
        final MutableListX<Painting> list = MutableListX.empty();

        final Painter expected = new Painter("Test", "Hallo", LocalDate.of(2000, 1, 1));

        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();

        final Painter painter = Transformable.from(vanGoghAuction)
                .apply(auction -> auction.setMostPopularPainting(Painting.of("Nijntje")))
                .run(PaintingAuction::getMostPopularPainting)
                .apply(It::println)
                .alsoUnless(Painting::isInMuseum, list::add)
                .run(Painting::painter)
                .let(p -> expected);

        assertAll(
                () -> assertTrue(list::isNotEmpty),
                () -> assertEquals(expected, painter)
        );
    }

    @Test
    void testToPair() {
        final var hallo = StringX.of("Hallo");

        final var pair = hallo.to("Joepie");

        assertEquals(Pair.of(hallo, "Joepie"), pair);
    }

    @Test
    void testAsList() {
        final var hallo = StringX.of("Hallo");

        final var actual = hallo.asListX();

        assertEquals(1, actual.size());
    }
}
