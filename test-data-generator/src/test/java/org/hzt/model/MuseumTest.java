package org.hzt.model;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class MuseumTest {

    @Test
    void testIteratingOverPaintingsInMuseum() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final Museum firstMuseum = museumList.stream()
                .findFirst()
                .orElseThrow();

        firstMuseum.forEach(System.out::println);

        final List<Painting> paintings = museumList.stream()
                .filter(museum -> Optional.ofNullable(museum).map(Museum::getDateOfOpening)
                        .filter(d -> d.isBefore(LocalDate.now()))
                        .isPresent())
                .mapMulti(Museum::forEach)
                .filter(painting -> painting.ageInYears() > 200)
                .toList();

        System.out.println("paintings = " + paintings);

        assertFalse(paintings.isEmpty());
    }

    @Test
    void testCreatingPaintingStreamFromMuseumSpliterator() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final Museum firstMuseum = museumList.stream()
                .findFirst()
                .orElseThrow();

        final List<Painting> paintings = StreamSupport.stream(firstMuseum.spliterator(), false).toList();

        assertFalse(paintings.isEmpty());
    }

}
