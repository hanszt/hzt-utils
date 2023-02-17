package org.hzt.model;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class MuseumTest {

    @Test
    void testIteratingOverPaintingsInMuseum() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var firstMuseum = museumList.stream()
                .findFirst()
                .orElseThrow();

        firstMuseum.forEach(System.out::println);

        final var paintings = museumList.stream()
                .filter(museum -> Optional.ofNullable(museum)
                        .map(Museum::getDateOfOpening)
                        .filter(LocalDate.parse("2023-02-03")::isAfter)
                        .isPresent())
                .flatMap(m -> StreamSupport.stream(m.spliterator(), false))
                .filter(painting -> painting.ageInYears() > 200)
                .collect(Collectors.toList());

        System.out.println("paintings = " + paintings);

        assertFalse(paintings.isEmpty());
    }

    @Test
    void testCreatingPaintingStreamFromMuseumSpliterator() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var firstMuseum = museumList.stream()
                .findFirst()
                .orElseThrow();

        final var paintings = StreamSupport.stream(firstMuseum.spliterator(), false)
                .collect(Collectors.toList());

        assertFalse(paintings.isEmpty());
    }

}
