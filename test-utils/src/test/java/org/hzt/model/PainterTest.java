package org.hzt.model;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PainterTest {

    @Test
    void testPainterEqualsItself() {
        Painter painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));

        assertEquals(painter1, painter1);
    }

    @Test
    void testPainterWithSameLastNameAndDateOfBirthEqualsOther() {
        Painter painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));
        Painter painter2 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));

        assertEquals(painter1, painter2);
    }

    @Test
    void testPainterWithDifferentDateOfBirthDoNotEqual() {
        Painter painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));
        Painter painter2 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.DECEMBER, 18));

        assertNotEquals(painter1, painter2);
    }

    @Test
    void testPaintersWithDifferentNameDoNotEqual() {
        Painter painter1 = new Painter("Piet", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));
        Painter painter2 = new Painter("Klaas", "Mondriaan", LocalDate.of(2021, Month.OCTOBER, 18));

        assertNotEquals(painter1, painter2);
    }

    @Test
    void testIterableOfPaintingInPainter() {
        final Painter picasso = TestSampleGenerator.createPaintingList().stream()
                .map(Painting::painter)
                .filter(painter -> painter.getLastname().equalsIgnoreCase("Picasso"))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        final List<Painting> paintings = StreamSupport.stream(picasso.spliterator(), false)
                .collect(Collectors.toList());

        picasso.forEach(System.out::println);

        assertFalse(paintings.isEmpty());


    }
}
