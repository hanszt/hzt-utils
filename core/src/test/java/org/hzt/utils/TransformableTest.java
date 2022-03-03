package org.hzt.utils;

import org.hzt.test.model.Painting;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.hzt.utils.test.model.PaintingAuction;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformableTest {

    @Test
    void testCreateATransformableOfSomethingAndUseTheDefaultFunctions() {
        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();
        final Painting nijntje = Painting.of("Nijntje");

        final LocalDate localDate = Transformable.from(vanGoghAuction)
                .apply(a -> a.setMostPopularPainting(nijntje))
                .let(PaintingAuction::getDateOfOpening);

        assertAll(
                () -> assertEquals(2, localDate.getDayOfMonth()),
                () -> assertEquals(nijntje, vanGoghAuction.getMostPopularPainting())
        );
    }

    @Test
    void testTransformablePipeline() {
        final MutableListX<Painting> list = MutableListX.empty();

        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();

        final String nijntje = "Nijntje";

        final String name = vanGoghAuction
                .apply(auction -> auction.setMostPopularPainting(Painting.of(nijntje)))
                .run(PaintingAuction::getMostPopularPainting)
                .apply(It::println)
                .alsoUnless(Painting::isInMuseum, list::add)
                .takeIf(Objects::nonNull)
                .map(Painting::name)
                .orElseThrow(NoSuchElementException::new);

        assertAll(
                () -> assertTrue(list::isNotEmpty),
                () -> assertEquals(nijntje, name)
        );
    }

    @Test
    void testToPair() {
        final StringX hallo = StringX.of("Hallo");

        final Pair<StringX, String> pair = hallo.to("Joepie");

        assertEquals(Pair.of(hallo, "Joepie"), pair);
    }
}
