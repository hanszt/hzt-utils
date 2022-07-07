package org.hzt.utils.sequences;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterators.FilteringIterator;
import org.hzt.utils.numbers.IntX;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomSequenceTest {

    @Test
    void testSumOfFloats() {
        final ListX<String> strings = ListX.of("This", "is", "processed", "by", "a", "custom", "Sequence");

        final float sum = CustomSequence.of(strings)
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
    }

    @FunctionalInterface
    private interface ToFloatFunction<T> {

        float applyAsFloat(T item);
    }
}
