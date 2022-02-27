package hzt.sequences.primitives;

import hzt.collections.primitives.LongListX;
import hzt.iterables.primitives.LongIterable;
import hzt.iterators.primitives.LongWindowedIterator;
import hzt.sequences.Sequence;
import hzt.sequences.SequenceHelper;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;
import java.util.function.ToLongFunction;

public interface LongWindowedSequence extends LongIterable {

    default Sequence<LongListX> chunked(int size) {
        return windowed(size, size, true);
    }

    default LongSequence chunked(int size, @NotNull ToLongFunction<LongListX> transform) {
        return windowed(size, size, true).mapToLong(transform);
    }

    default Sequence<LongListX> windowed(int size, int step, boolean partialWindows) {
        return windowed(size, It::asInt, step, It::asInt, partialWindows);
    }

    default Sequence<LongListX> windowed(int initSize,
                                        @NotNull IntUnaryOperator nextSizeSupplier,
                                        int initStep,
                                        @NotNull IntUnaryOperator nextStepSupplier,
                                        boolean partialWindows) {
        SequenceHelper.checkInitWindowSizeAndStep(initSize, initStep);
        return () -> LongWindowedIterator.of(iterator(), initSize, nextSizeSupplier, initStep, nextStepSupplier, partialWindows);
    }

    default Sequence<LongListX> windowed(int size, int step) {
        return windowed(size, step, false);
    }

    default Sequence<LongListX> windowed(int size) {
        return windowed(size, 1);
    }

    default Sequence<LongListX> windowed(int size, boolean partialWindows) {
        return windowed(size, 1, partialWindows);
    }

    default LongSequence windowed(int size, int step, boolean partialWindows,
                                  @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, step, partialWindows).mapToLong(reducer);
    }

    default LongSequence windowed(int size, int step, @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, step, false, reducer);
    }

    default LongSequence windowed(int size, @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, 1, reducer);
    }

    default LongSequence windowed(int size, boolean partialWindows, @NotNull ToLongFunction<LongListX> reducer) {
        return windowed(size, 1, partialWindows, reducer);
    }
}
