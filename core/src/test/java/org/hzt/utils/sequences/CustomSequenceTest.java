package org.hzt.utils.sequences;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterators.FilteringIterator;
import org.hzt.utils.numbers.IntX;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        final BigInteger[] seedValue = {BigInteger.ZERO, BigInteger.ONE};
        return Sequence
                .generate(seedValue, pair -> new BigInteger[]{pair[1], pair[0].add(pair[1])})
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

        static <T> CustomSequence<T> of(Iterable<T> iterable) {
            return iterable::iterator;
        }

        @Override
        default <R> CustomSequence<R> map(@NotNull Function<? super T, ? extends R> mapper) {
            return CustomSequence.of(Sequence.super.map(mapper));
        }

        @Override
        default CustomSequence<T> filter(@NotNull Predicate<? super T> predicate) {
            return () -> FilteringIterator.of(iterator(), predicate, true);
        }

        default float floatSumOf(@NotNull ToFloatFunction<? super T> selector) {
            float sum = 0;
            for (T t : this) {
                if (t != null) {
                    sum += selector.applyAsFloat(t);
                }
            }
            return sum;
        }

        static void main(String[] args) {
            fibonacciSequence()
                    .filter(bigInteger -> bigInteger.isProbablePrime(100))
                    .forEach(It::println);
        }
    }

    @FunctionalInterface
    private interface ToFloatFunction<T> {

        float applyAsFloat(T item);
    }

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

        for (var s : fizzBuzzer.take(3)) {
            It.println("s = " + s);
        }
        assertAll(
                () -> assertEquals("4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz, 16", actual),
                () -> assertEquals(20L, count)
        );
    }

    @FunctionalInterface
    private interface FizzBuzzer extends Sequence<String> {

        static FizzBuzzer start() {
            return Sequence.generate(() -> "")::iterator;
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

        @NotNull
        private String next(int index, String current, int modulo,  String string) {
            return next(index, current, modulo, 0, string);
        }

        @NotNull
        private String next(int index, String current, int modulo, int offSet, String string) {
            int value = index + 1;
            final var isNaturalNr = current.chars().allMatch(Character::isDigit);
            final var match = value % modulo == offSet;
            if (isNaturalNr) {
                return match ? string : String.valueOf(value);
            }
            return match ? current + string : current;
        }

        static void main(String[] args) {
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

            It.println(fizzBuzzer.take(2_000).joinToString());
            It.println("count = " + count);
            It.println("actual = " + actual);
        }
    }
}
