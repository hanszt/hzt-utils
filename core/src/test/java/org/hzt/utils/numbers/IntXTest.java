package org.hzt.utils.numbers;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IntXTest {

    @Test
    void testToDouble() {
        final DoubleX doubleX = IntX.of(10).toDoubleX()
                .toStringX().toDoubleX();

        assertEquals(DoubleX.of(10), doubleX);
    }

    @Test
    void testMapEachDigit() {
        final var count = StringX.of(1_003_293_342)
                .filter(Character::isDigit)
                .count();

        assertEquals(10, count);
    }

    @Test
    void testSequenceUntilNthPrimeNr() {
        final var oneThousandsPrimeNr = IntX.primeNrSequence()
                .take(1_000)
                .reduce((first, second) -> second)
                .orElseThrow();

        assertEquals(7919, oneThousandsPrimeNr);
    }

    @Test
    void testSieveOfEratosthenes() {
        final var primes = primeNrSequence(7919);

        final var count = primes.count();

        final var largestPrimeUnderOneThousand = primes
                .reduce((first, second) -> second)
                .orElseThrow();

        assertAll(
                () -> assertEquals(1000, count),
                () -> assertEquals(7919, largestPrimeUnderOneThousand)
        );
    }

    /**
     * using sieve of Eratosthenes
     * @param upperPrimeSize primes smaller or equal to this nr
     * @return IntSequence of primes
     */
    private static IntSequence primeNrSequence(@SuppressWarnings("SameParameterValue") int upperPrimeSize) {
        final var prime = new boolean[upperPrimeSize + 1];
        Arrays.fill(prime, true);

        for (int p = 2; p * p <= upperPrimeSize; p++) {
            // If prime[p] is not changed, then it is a prime
            if (prime[p]) {
                for (int i = p * p; i <= upperPrimeSize; i += p) {
                    prime[i] = false;
                }
            }
        }
        return IntSequence.generate(2, i -> i + (i < 3 ? 1 : 2))
                .takeWhile(i -> i <= upperPrimeSize)
                .filter(i -> prime[i]);
    }

    @Test
    void testAsChar() {
        String s = "This is a string";

        final var characters = s.chars()
                .mapToObj(IntX::asChar)
                .collect(Collectors.toList());

        System.out.println(characters);

        assertEquals(16, characters.size());
    }

}
