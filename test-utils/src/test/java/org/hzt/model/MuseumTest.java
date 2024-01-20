package org.hzt.model;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MuseumTest {

    @Test
    void testIteratingOverPaintingsInMuseum() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final List<String> paintingNames = museumList.stream()
                .filter(museum -> Optional.ofNullable(museum)
                        .map(Museum::getDateOfOpening)
                        .filter(LocalDate.parse("2023-02-03")::isAfter)
                        .isPresent())
                .flatMap(m -> StreamSupport.stream(m.spliterator(), false))
                .filter(painting -> painting.ageInYears(2023) > 200)
                .map(Painting::name)
                .collect(Collectors.toList());

        assertEquals(List.of("Meisje met de parel", "Het melkmeisje", "Meisje met de rode hoed"), paintingNames);
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
