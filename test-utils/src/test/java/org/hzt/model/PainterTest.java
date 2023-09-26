package org.hzt.model;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PainterTest {

    @Test
    void testPainterEqualsItself() {
        final var painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));

        assertEquals(painter1, painter1);
    }

    @Test
    void testPainterWithSameLastNameAndDateOfBirthEqualsOther() {
        final var painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));
        final var painter2 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));

        assertEquals(painter1, painter2);
    }

    @Test
    void testPainterWithDifferentDateOfBirthDoNotEqual() {
        final var painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));
        final var painter2 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.DECEMBER, 18));

        assertNotEquals(painter1, painter2);
    }

    @Test
    void testPaintersWithDifferentNameDoNotEqual() {
        final var painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));
        final var painter2 = new Painter("Klaas", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));

        assertNotEquals(painter1, painter2);
    }

    @Test
    void testIterableOfPaintingInPainter() {
        final var picasso = TestSampleGenerator.createPaintingList().stream()
                .map(Painting::painter)
                .filter(painter -> painter.getLastname().equalsIgnoreCase("Picasso"))
                .findFirst()
                .orElseThrow();

        final var paintings = StreamSupport.stream(picasso.spliterator(), false)
                .toList();

        picasso.forEach(System.out::println);

        assertFalse(paintings.isEmpty());


    }
}
