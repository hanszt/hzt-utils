package hzt.collections;

import org.junit.jupiter.api.Test;
import hzt.test.Generator;
import hzt.test.model.PaintingAuction;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class MutableListXTest {

    @Test
    void testMutableListX() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final IterableX<LocalDate> dates = museums.mapNotNull(PaintingAuction::getDateOfOpening);

        assertIterableEquals(expected, dates);
    }

    @Test
    void testListWithAll() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());
        expected.add(LocalDate.MIN);
        expected.add(LocalDate.MAX);

        final ListX<LocalDate> dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .plus(ListX.of(LocalDate.MIN, LocalDate.MAX));

        System.out.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testAlso() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());
        expected.add(LocalDate.MIN);

        final MutableListX<LocalDate> dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .also(localDates -> localDates.add(LocalDate.MIN));

        System.out.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testWhen() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        if (expected.isEmpty()) {
            expected.add(LocalDate.MIN);
        } else {
            expected.remove(0);
        }

        final MutableListX<LocalDate> dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .when(ListX::isNotEmpty, list -> list.remove(0))
                .when(list -> list.size() > 3, list -> list.add(LocalDate.MIN))
                .takeIf(ListX::isNotEmpty)
                .orElseThrow(NoSuchElementException::new);

        System.out.println("dates = " + dates);

        assertEquals(expected, dates);
    }

}
