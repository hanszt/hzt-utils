package hzt.sequences;

import hzt.collections.ListX;
import hzt.iterables.Windowable;
import hzt.iterators.WindowedIterator;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface WindowedSequence<T> extends Windowable<T> {

    default Sequence<ListX<T>> chunked(int size) {
        return windowed(size, size, true);
    }

    default Sequence<ListX<T>> chunked(int initSize, IntUnaryOperator nextSizeSupplier) {
        return windowed(initSize, nextSizeSupplier, initSize, nextSizeSupplier, true);
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
        return Sequence.of(windowed(initSize, nextSizeSupplier, step, It::asInt, partialWindows)).map(transform);
    }

    default Sequence<ListX<T>> windowed(int initSize,
                                        @NotNull IntUnaryOperator nextSizeSupplier,
                                        int initStep,
                                        @NotNull IntUnaryOperator nextStepSupplier,
                                        boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> WindowedIterator.of(iterator(),
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
