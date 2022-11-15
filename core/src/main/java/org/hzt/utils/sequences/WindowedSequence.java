package org.hzt.utils.sequences;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.Windowable;
import org.hzt.utils.It;
import org.hzt.utils.iterators.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface WindowedSequence<T> extends Windowable<T> {

    default Sequence<ListX<T>> chunked(int size) {
        return windowed(size, size, true);
    }

    default Sequence<ListX<T>> chunked(IntSupplier nextSizeSupplier) {
        return chunked(nextSizeSupplier.getAsInt(), size -> nextSizeSupplier.getAsInt());
    }

    default Sequence<ListX<T>> chunked(int initSize, IntUnaryOperator nextSizeSupplier) {
        //The holding consumer provides a way to provide the same value to both the size and step unary operator.
        //It also makes sure the nextSizeSupplier from this method is only called once instead of twice
        //It is required that the call for next size is made before next step for them to receive the same value
        final var holdingConsumer = new AtomicInteger();
        return windowed(initSize, size -> {
            final var nextSize = nextSizeSupplier.applyAsInt(size);
            holdingConsumer.set(nextSize);
            return nextSize;
        }, initSize, step -> holdingConsumer.get(), true);
    }

    default Sequence<ListX<T>> windowed(int size) {
        return windowed(size, 1);
    }

    default <R> Sequence<R> windowed(int size, @NotNull Function<? super ListX<T>, ? extends R> transform) {
        return windowed(size, 1).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default <R> Sequence<R> windowed(int size, int step, @NotNull Function<? super ListX<T>, ? extends R> transform) {
        return windowed(size, step, false).map(transform);
    }

    default Sequence<ListX<T>> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default Sequence<ListX<T>> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, step, partialWindows, It::self);
    }

    default <R> Sequence<R> windowed(int size, int step, boolean partialWindows,
                                     @NotNull Function<? super ListX<T>, R> transform) {
        return windowed(size, It::asInt, step, partialWindows, transform);
    }

    default <R> Sequence<R> windowed(int initSize,
                                     @NotNull IntUnaryOperator nextSizeSupplier,
                                     int step,
                                     boolean partialWindows,
                                     @NotNull Function<? super ListX<T>, R> transform) {
        return windowed(initSize, nextSizeSupplier, step, It::asInt, partialWindows).map(transform);
    }

    default Sequence<ListX<T>> windowed(int initSize,
                                        @NotNull IntUnaryOperator nextSizeSupplier,
                                        int initStep,
                                        @NotNull IntUnaryOperator nextStepSupplier,
                                        boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> Iterators.windowedIterator(iterator(),
                initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<ListX<T>> windowed(int initSize,
                                        @NotNull IntUnaryOperator nextSizeSupplier,
                                        int initStep,
                                        @NotNull IntUnaryOperator nextStepSupplier) {
        return windowed(initSize, nextSizeSupplier, initStep, nextStepSupplier, false);
    }

    default Sequence<ListX<T>> windowed(int initSize, @NotNull IntUnaryOperator nextSizeSupplier, int step) {
        return windowed(initSize, nextSizeSupplier, step, It::asInt, false);
    }

    default Sequence<ListX<T>> windowed(int size, int initStep, @NotNull IntUnaryOperator nextStepSupplier, boolean partialWindows) {
        return windowed(size, It::self, initStep, nextStepSupplier, partialWindows);
    }
}
