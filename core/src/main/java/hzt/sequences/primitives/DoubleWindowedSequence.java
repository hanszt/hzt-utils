package hzt.sequences.primitives;

import hzt.collections.primitives.DoubleListX;
import hzt.iterables.primitives.DoubleIterable;
import hzt.iterators.primitives.DoubleWindowedIterator;
import hzt.sequences.Sequence;
import hzt.sequences.SequenceHelper;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface DoubleWindowedSequence extends DoubleIterable {

    default Sequence<DoubleListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default DoubleSequence chunked(int size, @NotNull ToDoubleFunction<DoubleListX> transform) {
        return windowed(size, size, true).mapToDouble(transform);
    }

    default Sequence<DoubleListX> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<DoubleListX> windowed(int initSize,
                                           @NotNull IntUnaryOperator nextSizeSupplier,
                                           int initStep,
                                           @NotNull IntUnaryOperator nextStepSupplier,
                                           boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> DoubleWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<DoubleListX> windowed(int initSize,
                                           @NotNull IntUnaryOperator nextSizeSupplier,
                                           int initStep,
                                           @NotNull IntUnaryOperator nextStepSupplier) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> DoubleWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, false);
    }

    default Sequence<DoubleListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<DoubleListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<DoubleListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default DoubleSequence windowed(int size, int step, boolean partialWindows,
                                    @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, step, partialWindows).mapToDouble(reducer);
    }

    default DoubleSequence windowed(int size, int step, @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, step, false, reducer);
    }

    default DoubleSequence windowed(int size, @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, 1, reducer);
    }

    default DoubleSequence windowed(int size, boolean partialWindows, @NotNull ToDoubleFunction<DoubleListX> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
