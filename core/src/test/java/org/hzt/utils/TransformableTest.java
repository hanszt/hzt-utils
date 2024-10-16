package org.hzt.utils;

import org.hzt.test.model.Painting;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.hzt.utils.test.model.PaintingAuction;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformableTest {

    @Test
    void testCreateATransformableOfSomethingAndUseTheDefaultFunctions() {
        final PaintingAuction vanGoghAuction = Generator.createVanGoghAuction();
        final Painting nijntje = Painting.of("Nijntje");

        final LocalDate localDate = Transformable.from(vanGoghAuction)
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

    @Test
    void testTransformableSequence() {
        final Set<Integer> integers = TransformableSequence.of(Arrays.asList(1, 2, 3, 4, 3, 5))
                .alsoWhen(s -> s.count() > 10, s -> s.forEach(System.out::println))
                .get()
                .toSet();

        assertEquals(new HashSet<>(Arrays.asList(1,2,3,4,5)), integers);
    }

    interface TransformableSequence<T> extends Transformable<TransformableSequence<T>>, Sequence<T> {

        static <T> TransformableSequence<T> of(final Iterable<T> iterable) {
            return iterable::iterator;
        }

        @Override
        default TransformableSequence<T> get() {
            return this;
        }
    }
}
