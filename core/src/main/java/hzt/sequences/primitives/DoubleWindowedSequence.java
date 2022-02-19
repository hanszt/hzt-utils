package hzt.sequences.primitives;

import hzt.PreConditions;
import hzt.iterators.primitives.DoubleWindowedIterator;
import hzt.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class DoubleWindowedSequence implements Sequence<DoubleSequence> {

    private final DoubleSequence upstream;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    public DoubleWindowedSequence(DoubleSequence upstream, int size, int step, boolean partialWindows) {
        checkWindowSizeAndStep(size, step);
        this.upstream = upstream;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
    }

    private static void checkWindowSizeAndStep(int size, int step) {
        PreConditions.require(size > 0 && step > 0, () -> getErrorMessage(size, step));

    }

    private static String getErrorMessage(int size, int step) {
        if (size != step) {
            return "Both size " + size + " and step " + step + " must be greater than zero.";
        }
        return "size " + size + " must be greater than zero.";
    }

    @NotNull
    @Override
    public Iterator<DoubleSequence> iterator() {
        return DoubleWindowedIterator.of(upstream.iterator(), size, step, partialWindows);
    }
}
