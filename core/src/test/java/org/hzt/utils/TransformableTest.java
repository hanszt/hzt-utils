package org.hzt.utils;

import org.hzt.test.model.Painting;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.hzt.utils.test.model.PaintingAuction;
import org.hzt.utils.tuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransformableTest {

    @Test
    void testCreateATransformableOfSomethingAndUseTheDefaultFunctions() {
        final var vanGoghAuction = Generator.createVanGoghAuction();
        final var nijntje = Painting.of("Nijntje");

        final var localDate = Transformable.from(vanGoghAuction)
                .apply(auction -> auction.setMostPopularPainting(nijntje))
                .let(PaintingAuction::getDateOfOpening);

        assertAll(
                () -> assertEquals(2, localDate.getDayOfMonth()),
                () -> assertEquals(nijntje, vanGoghAuction.getMostPopularPainting())
        );
    }

    @Test
    void testTransformablePipeline() {
        final MutableListX<Painting> list = MutableListX.empty();

        final var vanGoghAuction = Generator.createVanGoghAuction();

        final var nijntje = "Nijntje";

        vanGoghAuction
                .apply(auction -> auction.setMostPopularPainting(Painting.of(nijntje)))
                .run(PaintingAuction::getMostPopularPainting)
                .apply(It::println)
                .alsoUnless(Painting::isInMuseum, list::add)
                .takeIf(Objects::nonNull)
                .map(Painting::name)
                .ifPresentOrElse(name -> assertAll(
                        () -> assertTrue(list::isNotEmpty),
                        () -> assertEquals(nijntje, name)
                ), Assertions::fail);

    }

    @Test
    void testToPair() {
        final var hallo = StringX.of("Hallo");

        final var pair = hallo.to("Joepie");

        assertEquals(Pair.of(hallo, "Joepie"), pair);
    }

    @Test
    void testTransformableSequence() {
        final var integers = TransformableSequence.of(IntList.of(1, 2, 3, 4, 3, 5))
                .when(s -> s.count() > 4, s -> s.forEach(System.out::println))
                .toSet();

        assertEquals(Set.of(1,2,3,4,5), integers);
    }

    interface TransformableSequence<T> extends Transformable<TransformableSequence<T>>, Sequence<T> {

        static <T> TransformableSequence<T> of(Iterable<T> iterable) {
            return iterable::iterator;
        }

        @Override
        @NotNull
        default TransformableSequence<T> get() {
            return this;
        }
    }
}
