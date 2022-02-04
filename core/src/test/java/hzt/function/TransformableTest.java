package hzt.function;

import hzt.collections.MutableListX;
import hzt.strings.StringX;
import hzt.test.Generator;
import hzt.test.model.PaintingAuction;
import hzt.tuples.Pair;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformableTest {

    @Test
    void testCreateATransformableOfSomethingAndUseTheDefaultFunctions() {
        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();

        final LocalDate localDate = Transformable.of(vanGoghAuction)
                .apply(a -> a.setMostPopularPainting(Painting.of("Nijntje")))
                .let(PaintingAuction::getDateOfOpening);

        assertEquals(2, localDate.getDayOfMonth());
    }

    @Test
    void testAlsoWhen() {
        final MutableListX<Painting> list = MutableListX.empty();

        final Painter expected = new Painter("Test", "Hallo", LocalDate.of(2000, 1, 1));

        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();

        final Painter painter = Transformable.of(vanGoghAuction)
                .apply(auction -> auction.setMostPopularPainting(Painting.of("Nijntje")))
                .run(PaintingAuction::getMostPopularPainting)
                .apply(System.out::println)
                .alsoUnless(Painting::isInMuseum, list::add)
                .run(Painting::painter)
                .let(p -> expected);

        assertTrue(list::isNotEmpty);
        assertEquals(expected, painter);
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
