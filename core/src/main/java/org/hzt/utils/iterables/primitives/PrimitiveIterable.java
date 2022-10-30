package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterators.primitives.PrimitiveAtomicIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public interface PrimitiveIterable<T, C> extends Iterable<T> {

    PrimitiveAtomicIterator<T, C> atomicIterator();

    @Override
    Spliterator<T> spliterator();
    
    @FunctionalInterface
    interface OfInt extends PrimitiveIterable<Integer, IntConsumer> {

        PrimitiveIterator.OfInt iterator();

        default PrimitiveAtomicIterator.OfInt atomicIterator() {
            return PrimitiveAtomicIterator.of(iterator());
        }

        default void forEachInt(@NotNull IntConsumer action) {
            var intIterator = iterator();
            while (intIterator.hasNext()) {
                action.accept(intIterator.nextInt());
            }
        }

        @Override
        default void forEach(Consumer<? super Integer> action) {
            var intIterator = iterator();
            if (intIterator.hasNext()) {
                throw new UnsupportedOperationException("Use forEachInt instead");
            }
        }

        @Override
        default Spliterator.OfInt spliterator() {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }
    }

    @FunctionalInterface
    interface OfLong extends PrimitiveIterable<Long, LongConsumer> {

        PrimitiveIterator.OfLong iterator();

        default PrimitiveAtomicIterator.OfLong atomicIterator() {
            return PrimitiveAtomicIterator.of(iterator());
        }

        default void forEachLong(@NotNull LongConsumer action) {
            var intIterator = iterator();
            while (intIterator.hasNext()) {
                action.accept(intIterator.nextLong());
            }
        }

        @Override
        default void forEach(Consumer<? super Long> action) {
            var intIterator = iterator();
            if (intIterator.hasNext()) {
                throw new UnsupportedOperationException("Use forEachLong instead");
            }
        }

        @Override
        default Spliterator.OfLong spliterator() {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }
    }

    @FunctionalInterface
    interface OfDouble extends PrimitiveIterable<Double, DoubleConsumer> {

        PrimitiveIterator.OfDouble iterator();

        default PrimitiveAtomicIterator.OfDouble atomicIterator() {
            return PrimitiveAtomicIterator.of(iterator());
        }

        default void forEachDouble(@NotNull DoubleConsumer action) {
            var intIterator = iterator();
            while (intIterator.hasNext()) {
                action.accept(intIterator.nextDouble());
            }
        }

        @Override
        default void forEach(Consumer<? super Double> action) {
            var intIterator = iterator();
            if (intIterator.hasNext()) {
                throw new UnsupportedOperationException("Use forEachDouble instead");
            }
        }

        @Override
        default Spliterator.OfDouble spliterator() {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }
    }
}
