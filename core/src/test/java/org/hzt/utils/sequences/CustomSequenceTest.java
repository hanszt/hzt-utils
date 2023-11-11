package org.hzt.utils.sequences;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterators.Iterators;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.tuples.IndexedValue;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class CustomSequenceTest {

    @Test
    void testBigIntFibonacciSequencePrimes() {
        final var probableFibNrPrimeCount = fibonacciSequence()
                .filter(fibNr -> fibNr.isProbablePrime(100))
                .map(BigInteger::toString)
                .onEach(It::println)
                .takeWhile(fibNr -> fibNr.length() <= 100)
                .count();

        assertEquals(18, probableFibNrPrimeCount);
    }

    static Sequence<BigInteger> fibonacciSequence() {
        final var seedValue = new BigInteger[]{BigInteger.ZERO, BigInteger.ONE};
        return Sequence
                .iterate(seedValue, pair -> new BigInteger[]{pair[1], pair[0].add(pair[1])})
                .map(pair -> pair[0]);
    }

    @Test
    void testSumOfFloats() {
        final var strings = ListX.of("This", "is", "processed", "by", "a", "custom", "Sequence");

        final var sum = CustomSequence.of(strings)
                .map(String::length)
                .filter(IntX::isEven)
                .floatSumOf(Integer::floatValue);

        assertEquals(22F, sum);
    }

    private interface CustomSequence<T> extends Sequence<T> {

        static <T> CustomSequence<T> of(final Iterable<T> iterable) {
            return iterable::iterator;
        }

        @Override
        default <R> CustomSequence<R> map(final Function<? super T, ? extends R> mapper) {
            return CustomSequence.of(Sequence.super.map(mapper));
        }

        @Override
        default CustomSequence<T> filter(final Predicate<? super T> predicate) {
            return () -> Iterators.filteringIterator(iterator(), predicate, true);
        }

        default float floatSumOf(final ToFloatFunction<? super T> selector) {
            float sum = 0;
            for (final var t : this) {
                if (t != null) {
                    sum += selector.applyAsFloat(t);
                }
            }
            return sum;
        }

        static void main(final String[] args) {
            fibonacciSequence()
                    .filter(bigInteger -> bigInteger.isProbablePrime(100))
                    .forEach(It::println);
        }
    }

    @FunctionalInterface
    private interface ToFloatFunction<T> {

        float applyAsFloat(T item);
    }

    private static boolean isNaturalNr(final String current) {
        return current.chars().allMatch(Character::isDigit);
    }

    @Nested
    class FizzBuzzerTests {

        @Test
        void testFizzBuzzer() {
            final var fizzBuzzer = FizzBuzzer
                    .start()
                    .fizz()
                    .buzz();

            final var count = fizzBuzzer
                    .take(100)
                    .filter(s -> s.contains("buzz"))
                    .count();

            final var actual = fizzBuzzer
                    .take(16)
                    .skip(3)
                    .joinToString(", ");

            for (final var s : fizzBuzzer.take(3)) {
                println("s = " + s);
            }
            assertAll(
                    () -> assertEquals("4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz, 16", actual),
                    () -> assertEquals(20L, count)
            );
        }

        @TestFactory
        Sequence<DynamicTest> testFizzBuzzerContainsBuzzAtMultipleOf5() {
            return FizzBuzzer.start()
                    .fizz()
                    .buzz()
                    .withIndex()
                    .onEach(It::println)
                    .filter(value -> (value.index() + 1) % 5 == 0)
                    .map(this::everyFifthContainsBuzz)
                    .take(100);
        }

        private DynamicTest everyFifthContainsBuzz(final IndexedValue<String> indexedValue) {
            final var n = indexedValue.index() + 1;
            final var name = "Value at n=" + n + " contains buzz";
            return dynamicTest(name, () -> assertTrue(indexedValue.value().contains("buzz")));
        }

        @TestFactory
        Sequence<DynamicTest> testFizzBuzzerContainsFizzAtMultipleOf3() {
            return FizzBuzzer.start()
                    .fizz()
                    .buzz()
                    .withIndex()
                    .onEach(It::println)
                    .filter(value -> (value.index() + 1) % 3 == 0)
                    .map(this::everyThirdContainsFizz)
                    .take(100);
        }

        private DynamicTest everyThirdContainsFizz(final IndexedValue<String> indexedValue) {
            final var n = indexedValue.index() + 1;
            final var name = "Value at n=" + n + " contains fizz";
            return dynamicTest(name, () -> assertTrue(indexedValue.value().contains("fizz")));
        }
    }

    @FunctionalInterface
    private interface FizzBuzzer extends Sequence<String> {

        static FizzBuzzer start() {
            return IntSequence.iterate(1, n -> n + 1).mapToObj(String::valueOf)::iterator;
        }

        default FizzBuzzer fizz() {
            return mapIndexed((index, value) -> next(index, value, 3, "fizz"))::iterator;
        }

        default FizzBuzzer buzz() {
            return mapIndexed((index, value) -> next(index, value, 5, "buzz"))::iterator;
        }

        default FizzBuzzer bizz() {
            return mapIndexed((index, value) -> next(index, value, 7, "bizz"))::iterator;
        }

        default FizzBuzzer even() {
            return mapIndexed((index, value) -> next(index, value, 2, "even"))::iterator;
        }

        default FizzBuzzer odd() {
            return mapIndexed((index, value) -> next(index, value, 2, 1, "odd"))::iterator;
        }

        private static String next(final int index, final String current, final int modulo, final String string) {
            return next(index, current, modulo, 0, string);
        }

        private static String next(final int index, final String current, final int modulo, final int offSet, final String string) {
            final var value = index + 1;
            final var isNaturalNr = isNaturalNr(current);
            final var match = value % modulo == offSet;
            if (isNaturalNr) {
                return match ? string : String.valueOf(value);
            }
            return match ? current + string : current;
        }

        static void main(final String[] args) {
            final var fizzBuzzer = FizzBuzzer
                    .start()
                    .fizz()
                    .bizz()
                    .even()
                    .buzz()
                    .odd();

            final var count = fizzBuzzer
                    .take(100_000)
                    .filter(s -> s.equals("fizzbizzevenbuzz"))
                    .count();

            final var actual = fizzBuzzer
                    .take(16)
                    .skip(3)
                    .joinToString(", ");

            println(fizzBuzzer.take(2_000).joinToString());
            println("count = " + count);
            println("actual = " + actual);
        }
    }
}
