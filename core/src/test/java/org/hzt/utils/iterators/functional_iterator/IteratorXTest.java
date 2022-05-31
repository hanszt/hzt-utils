package org.hzt.utils.iterators.functional_iterator;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.numbers.IntX;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class IteratorXTest {

    @Test
    void testFunctionalIteratorImpl() {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        final int upperBound = 100;

        IteratorX<String> stringIterator = getBoundedStringIteratorX(upperBound);
        //noinspection StatementWithEmptyBody
        while (stringIterator.tryAdvance(list1::add)) ;

        final IteratorX<String> boundedIteratorX = getBoundedStringIteratorX(upperBound);
        final Iterable<String> stringIterable = boundedIteratorX::asIterator;
        stringIterable.forEach(list2::add);

        assertEquals(list1, list2);
    }

    @Test
    void testForEachRemaining() {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();

        final int upperBound = 200;
        IteratorX<String> stringIterator = getBoundedStringIteratorX(upperBound);
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
        final int bound = 241;
        AtomicInteger atomicInteger = new AtomicInteger();

        IteratorX<String> stringIterator = getBoundedStringIteratorX(bound);
        stringIterator.asIterator().forEachRemaining(e -> atomicInteger.incrementAndGet());

        assertEquals(bound, atomicInteger.get());
    }

    @NotNull
    private IteratorX<String> getBoundedStringIteratorX(int upperBound) {
        return new BoundedIterator<>(upperBound, String::valueOf)::supplyNext;
    }

    @Test
    void testMappingIterator() {
        final ListX<String> strings = ListX.of("This", "is", "a", "test");

        final IteratorX<String> stringIteratorX = strings.iteratorX();

        final IteratorX<Integer> toLengthMapperIterator = action -> {
            final AtomicReference<String> consumer = new AtomicReference<>();
            final boolean advance = stringIteratorX.tryAdvance(consumer::set);
            if (advance) {
                action.accept(consumer.get().length());
            }
            return advance;
        };
        Iterable<Integer> integers = toLengthMapperIterator::asIterator;

        assertIterableEquals(Arrays.asList(4, 2, 1, 4), integers);
    }

    private static class BoundedIterator<T> {

        private final int upperBound;
        private final IntFunction<T> supplier;
        private int counter = 0;

        private static <T> BoundedIterator<T> of(int upperBound, IntFunction<T> supplier) {
            return new BoundedIterator<>(upperBound, supplier);
        }

        private BoundedIterator(int upperBound, IntFunction<T> supplier) {
            this.upperBound = upperBound;
            this.supplier = supplier;
        }

        private boolean supplyNext(Consumer<? super T> consumer) {
            final boolean supplyNext = counter < upperBound;
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
        final int upperBound = 9_000;

        IteratorX<LocalDate> dateIterator = BoundedIterator.of(upperBound, LocalDate
                .parse("2022-04-21")::minusDays)
                ::supplyNext;

        final int[] ints = StreamSupport.stream(dateIterator.asSpliterator(), false)
                .mapToInt(LocalDate::getDayOfMonth)
                .filter(IntX::isEven)
                .toArray();

        assertEquals(4410, ints.length);
    }

    @Test
    @DisplayName("Test iterator from empty iteratorX throws no such element exception")
    void testIteratorFromEmptyIteratorXThrowsNoSuchElementException() {
        IteratorX<String> iteratorX = c -> false;
        final Iterator<String> stringIterator = iteratorX.asIterator();

        assertAll(
                () -> assertFalse(stringIterator.hasNext()),
                () -> assertThrows(NoSuchElementException.class, stringIterator::next)
        );
    }

    @Test
    @DisplayName("Test iterator from single element iteratorX throws no such element exception after 1")
    void testIteratorFromOneThrowsNoSuchElementExceptionAfterOne() {
        IteratorX<String> iteratorX = BoundedIterator.of(1, String::valueOf)::supplyNext;
        final Iterator<String> stringIterator = iteratorX.asIterator();

        final boolean hasOne = stringIterator.hasNext();
        final String first = stringIterator.next();

        assertAll(
                () -> assertTrue(hasOne),
                () -> assertEquals("0", first),
                () -> assertFalse(stringIterator.hasNext()),
                () -> assertThrows(NoSuchElementException.class, stringIterator::next)
        );
    }
}
