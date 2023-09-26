package org.hzt.utils.iterators.functional_iterator;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.numbers.IntX;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtomicIteratorTest {

    @Test
    void testAtomicIteratorBehavesTheSameAsIterator() {
        final List<String> list1 = new ArrayList<>();
        final List<String> list2 = new ArrayList<>();

        final var upperBound = 100;

        final var stringIterator = getBoundedAtomicIteratorIteratorX(upperBound, String::valueOf);
        //noinspection StatementWithEmptyBody
        while (stringIterator.tryAdvance(list1::add)) ;

        final var boundedIteratorX = getBoundedAtomicIteratorIteratorX(upperBound, String::valueOf);
        final Iterable<String> stringIterable = boundedIteratorX::asIterator;
        stringIterable.forEach(list2::add);

        assertEquals(list1, list2);
    }

    @Test
    void testForEachRemaining() {
        final List<String> list1 = new ArrayList<>();
        final List<String> list2 = new ArrayList<>();

        final var upperBound = 200;
        final var stringIterator = getBoundedAtomicIteratorIteratorX(upperBound, String::valueOf);
        stringIterator.forEachRemaining(list1::add);
        stringIterator.forEachRemaining(list2::add);

        assertAll(
                () -> assertEquals(upperBound, list1.size()),
                () -> assertTrue(list2.isEmpty())
        );
    }

    @Test
    @DisplayName("Test get correct iterator from iteratorX instance")
    void testGetCorrectIteratorFromIteratorXInstance() {
        final var bound = 241;
        final var atomicInteger = new AtomicInteger();

        final var stringIterator = getBoundedAtomicIteratorIteratorX(bound, String::valueOf);
        stringIterator.asIterator().forEachRemaining(e -> atomicInteger.incrementAndGet());

        assertEquals(bound, atomicInteger.get());
    }

    @NotNull
    private AtomicIterator<String> getBoundedAtomicIteratorIteratorX(final int upperBound, final IntFunction<String> intFunction) {
        return new BoundedIterator<>(upperBound, intFunction)::supplyNext;
    }

    @Test
    void testMappingIterator() {
        final var strings = ListX.of("This", "is", "a", "test");

        final var stringIteratorX = strings.atomicIterator();

        final AtomicIterator<Integer> toLengthMapperIterator = action -> {
            final var consumer = new AtomicReference<String>();
            final var advance = stringIteratorX.tryAdvance(consumer::set);
            if (advance) {
                action.accept(consumer.get().length());
            }
            return advance;
        };
        final Iterable<Integer> integers = toLengthMapperIterator::asIterator;

        assertIterableEquals(List.of(4, 2, 1, 4), integers);
    }

    private static class BoundedIterator<T> {

        private final int upperBound;
        private final IntFunction<T> supplier;
        private int counter = 0;

        private static <T> BoundedIterator<T> of(final int upperBound, final IntFunction<T> supplier) {
            return new BoundedIterator<>(upperBound, supplier);
        }

        private BoundedIterator(final int upperBound, final IntFunction<T> supplier) {
            this.upperBound = upperBound;
            this.supplier = supplier;
        }

        private boolean supplyNext(final Consumer<? super T> consumer) {
            final var supplyNext = counter < upperBound;
            if (supplyNext) {
                consumer.accept(supplier.apply(counter));
            }
            counter++;
            return supplyNext;
        }
    }

    @Test
    @DisplayName("Test as spliterator")
    void testAsSpliterator() {
        final var upperBound = 9_000;

        final AtomicIterator<LocalDate> dateIterator = BoundedIterator.of(upperBound, LocalDate
                .parse("2022-04-21")::minusDays)
                ::supplyNext;

        final var ints = StreamSupport.stream(dateIterator.asSpliterator(), false)
                .mapToInt(LocalDate::getDayOfMonth)
                .filter(IntX::isEven)
                .toArray();

        assertEquals(4410, ints.length);
    }

    @Test
    @DisplayName("Test iterator from empty iteratorX throws no such element exception")
    void testIteratorFromEmptyIteratorXThrowsNoSuchElementException() {
        final AtomicIterator<String> atomicIterator = c -> false;
        final var stringIterator = atomicIterator.asIterator();

        assertAll(
                () -> assertFalse(stringIterator.hasNext()),
                () -> assertThrows(NoSuchElementException.class, stringIterator::next)
        );
    }

    @Test
    @DisplayName("Test iterator from single element iteratorX throws no such element exception after 1")
    void testIteratorFromOneThrowsNoSuchElementExceptionAfterOne() {
        final AtomicIterator<String> atomicIterator = BoundedIterator.of(1, String::valueOf)::supplyNext;
        final var stringIterator = atomicIterator.asIterator();

        final var hasOne = stringIterator.hasNext();
        final var first = stringIterator.next();

        assertAll(
                () -> assertTrue(hasOne),
                () -> assertEquals("0", first),
                () -> assertFalse(stringIterator.hasNext()),
                () -> assertThrows(NoSuchElementException.class, stringIterator::next)
        );
    }

    @Test
    void testListContainingNullsCanBeTraversedByAtomicIterator() {
        final var listContainingNulls = Arrays.asList(null, 2, 3, 4, null, 7);
        final var atomicIterator = AtomicIterator.of(listContainingNulls.iterator());
        final var result = new ArrayList<Integer>();
        //noinspection StatementWithEmptyBody
        while (atomicIterator.tryAdvance(result::add));
        assertEquals(listContainingNulls, result);
    }
}
