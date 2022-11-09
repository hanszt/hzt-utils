package org.hzt.utils.function;

import org.hzt.test.model.Person;
import org.hzt.utils.It;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.DoubleRange;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.hzt.utils.function.Functions.*;
import static org.hzt.utils.function.Functions.distinctBy;
import static org.hzt.utils.function.Functions.not;
import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {

    @Test
    void testBiPredicateNot() {
        final var list = Sequence.of(1, 2, 3, 4, 4, 8, 10, 6, 5)
                .zipWithNext()
                .takeWhile(not(Integer::equals))
                .merge()
                .toList();

        System.out.println("list = " + list);

        assertFalse(list.contains(8));
    }

    @Test
    void testDistinctBy() {
        final Person[] people1 = {new Person("Piet"), new Person("Piet"), new Person("Klaas")};

        final var distinctByName = Stream.of(people1)
                .filter(distinctBy(Person::getName))
                .toArray(Person[]::new);

        assertEquals(2, distinctByName.length);
    }

    @Test
    void testIntPredicateNot() {
        final var allIntsAreOdd = IntStream.range(0, 100)
                .filter(notInt(IntX::isEven))
                .noneMatch(IntX::isEven);

        assertTrue(allIntsAreOdd);
    }

    @Test
    void testLongPredicateNot() {
        final var allLongsAreOdd = LongStream.range(0, 100L)
                .filter(notLong(LongX::isEven))
                .noneMatch(LongX::isEven);

        assertTrue(allLongsAreOdd);
    }

    @Test
    void testDoublePredicateNot() {
        final var doubleRange = DoubleRange.closed(.3, .7);

        final var allDoublesOutsideRange = DoubleStream.generate(Math::random)
                .limit(1_000)
                .filter(notDouble(doubleRange::contains))
                .peek(It::println)
                .noneMatch(doubleRange::contains);

        assertTrue(allDoublesOutsideRange);
    }
}
