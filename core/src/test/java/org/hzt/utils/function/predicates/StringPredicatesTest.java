package org.hzt.utils.function.predicates;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.function.Functions.by;
import static org.hzt.utils.function.predicates.StringPredicates.containsAllOf;
import static org.hzt.utils.function.predicates.StringPredicates.containsAnyOf;
import static org.hzt.utils.function.predicates.StringPredicates.containsNoneOf;
import static org.hzt.utils.function.predicates.StringPredicates.endsWithAnyOf;
import static org.hzt.utils.function.predicates.StringPredicates.hasEqualLength;
import static org.hzt.utils.function.predicates.StringPredicates.isEqualIgnoreCase;
import static org.hzt.utils.function.predicates.StringPredicates.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringPredicatesTest {

    @Test
    void testContains() {
        final var O = "o";
        final var A = "first";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> painting.name().contains(O) || painting.name().contains(A))
                .toList();

        final var result = paintingList.stream()
                .filter(by(Painting::name, containsAnyOf(O, A)))
                .toList();

        result.forEach(It::println);
        assertEquals(expected, result);
    }

    @Test
    void testIsEqualIgnoreCase() {
        final var NAME1 = "Meisje Met dE paRel";
        final var NAME2 = "GUERNICA";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> painting.name().equalsIgnoreCase(NAME1) || painting.name().equalsIgnoreCase(NAME2))
                .toList();

        final var result = paintingList.stream()
                .filter(by(Painting::name, isEqualIgnoreCase(NAME1).or(isEqualIgnoreCase(NAME2))))
                .toList();

        result.forEach(It::println);
        assertEquals(expected, result);
    }

    @Test
    void testStartsWith() {
        final var LE = "Le";
        final var ME = "Me";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> painting.name().startsWith(LE) || painting.name().startsWith(ME))
                .toList();

        final var result = paintingList.stream()
                .filter(by(Painting::name, startsWith(LE).or(startsWith(ME))))
                .toList();

        result.forEach(It::println);
        assertEquals(expected, result);
    }

    @Test
    void testEndsWith() {
        final var EL = "el";
        final var HOED = "hoed";
        final var NON = "non";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> painting.name().endsWith(EL) || painting.name().endsWith(HOED) || painting.name().endsWith(NON))
                .toList();

        final var result = paintingList.stream()
                .filter(by(Painting::name, endsWithAnyOf(EL, HOED, NON)))
                .toList();

        result.forEach(It::println);
        assertEquals(expected, result);
    }

    @Test
    void testHasEqualLength() {
        final var NAME = "Meisje met de parel";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> painting.name().length() == NAME.length())
                .toList();

        final var result = paintingList.stream()
                .filter(by(Painting::name, hasEqualLength(NAME)))
                .toList();

        result.forEach(It::println);

        assertEquals(expected, result);
    }

    @Test
    void testStringContainsAll() {
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> {
                    final var containsMeisje = painting.name().contains("Meisje");
                    final var containsDe = painting.name().contains("de");
                    final var containsA = painting.name().contains("first");
                    return containsMeisje && containsDe && containsA;
                }).toList();

        final var result = paintingList.stream()
                .filter(by(Painting::name, containsAllOf("Meisje", "de", "first")))
                .toList();

        result.forEach(It::println);
        assertEquals(expected, result);
    }

    @Test
    void testContainsAnyOf() {
        //arrange
        final var EL = "el";
        final var HOED = "hoed";
        final var NON = "non";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> painting.name().contains(EL) || painting.name().contains(HOED) || painting.name().contains(NON))
                .toList();
        //act
        final var result = paintingList.stream()
                .filter(by(Painting::name, containsAnyOf(EL, HOED, NON)))
                .toList();

        It.println("Input:");
        paintingList.forEach(It::println);
        It.println("result:");
        result.forEach(It::println);
        //assert
        assertEquals(expected, result);
    }

    @Test
    void testContainsNone() {
        //arrange
        final var EL = "el";
        final var HOED = "hoed";
        final var NON = "non";
        final var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .filter(painting -> !(painting.name().contains(EL) || painting.name().contains(HOED) || painting.name().contains(NON)))
                .toList();
        //act
        final var result = paintingList.stream()
                .filter(by(Painting::name, containsNoneOf(EL, HOED, NON)))
                .toList();

        It.println("Input:");
        paintingList.forEach(It::println);
        It.println("result:");
        result.forEach(It::println);
        //assert
        assertEquals(expected, result);
    }
}
