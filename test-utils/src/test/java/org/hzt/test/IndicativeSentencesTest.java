package org.hzt.test;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicativeSentencesTest {


    @Nested
    @DisplayNameGeneration(IndicativeSentences.class)
    class ANumberIsFizz {
        @Test
        void ifItIsDivisibleByThree() {
            final String result = fizzbuzz(99);
            assertEquals("fizz", result);
        }

        @ParameterizedTest(name = "Number {0} is fizz.")
        @ValueSource(ints = {3, 12, 18})
        void ifItIsOneOfTheFollowingNumbers(final int number) {
            final String result = fizzbuzz(number);
            assertEquals("fizz", result);
        }
    }

    @Nested
    @DisplayNameGeneration(IndicativeSentences.class)
    class ANumberContainsBuzz {
        @Test
        void ifItIsDivisibleByFive() {
            final String result = fizzbuzz(105);
            assertTrue(result.contains("buzz"));
        }

        @ParameterizedTest(name = "Number {0} contains buzz.")
        @ValueSource(ints = {5, 10, 15, 20})
        void ifItIsOneOfTheFollowingNumbers(final int number) {
            final String result = fizzbuzz(number);
            assertTrue(result.contains("buzz"));
        }
    }

    static String fizzbuzz(final int nr) {
        final StringBuilder sb = new StringBuilder();
        if (nr % 3 == 0) {
            sb.append("fizz");
        }
        if (nr % 5 == 0) {
            sb.append("buzz");
        }
        if (nr % 3 != 0 && nr % 5 != 0) {
            sb.append(nr);
        }
        return sb.toString();
    }

}
