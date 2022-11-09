package org.hzt.utils.streams;

import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.iterators.primitives.PrimitiveAtomicIterator;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;

@FunctionalInterface
public interface Spliterable<T> extends Iterable<T> {

    @Override
    Spliterator<T> spliterator();

    default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }

    default AtomicIterator<T> atomicIterator() {
        return AtomicIterator.of(spliterator());
    }

    @FunctionalInterface
    interface OfInt extends Spliterable<Integer>, PrimitiveIterable.OfInt {

        Spliterator.OfInt spliterator();

        @Override
        default PrimitiveIterator.OfInt iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        default PrimitiveAtomicIterator.OfInt atomicIterator() {
            return PrimitiveAtomicIterator.of(iterator());
        }
    }

    @FunctionalInterface
    interface OfLong extends Spliterable<Long>, PrimitiveIterable.OfLong {

        Spliterator.OfLong spliterator();

        @Override
        default PrimitiveIterator.OfLong iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        default PrimitiveAtomicIterator.OfLong atomicIterator() {
            return PrimitiveAtomicIterator.of(iterator());
        }
    }

    @FunctionalInterface
    interface OfDouble extends Spliterable<Double>, PrimitiveIterable.OfDouble {

        Spliterator.OfDouble spliterator();

        @Override
        default PrimitiveIterator.OfDouble iterator() {
            return Spliterators.iterator(spliterator());
        }

        @Override
        default PrimitiveAtomicIterator.OfDouble atomicIterator() {
            return PrimitiveAtomicIterator.of(iterator());
        }
    }
}
