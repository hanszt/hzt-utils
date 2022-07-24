package org.hzt.utils.function;

import org.hzt.test.model.Person;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;
import java.util.List;

import static org.hzt.utils.function.Functions.distinctBy;
import static org.hzt.utils.function.Functions.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FunctionsTest {

    @Test
    void testBiPredicateNot() {
        final List<Integer> list = Sequence.of(1, 2, 3, 4, 4, 8, 10, 6, 5)
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
}
