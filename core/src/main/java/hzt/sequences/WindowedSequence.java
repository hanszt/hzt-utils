package hzt.sequences;

import hzt.PreConditions;
import hzt.collections.ListX;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class WindowedSequence<T> implements Sequence<ListX<T>> {

    private final Sequence<T> upstream;
    private final int size;
    private final int step;
    private final boolean partialWindows;

    public WindowedSequence(Sequence<T> upstream, int size, int step, boolean partialWindows) {
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
    public Iterator<ListX<T>> iterator() {
        return new WindowedIterator<>(upstream, size, step, partialWindows);
    }
}
