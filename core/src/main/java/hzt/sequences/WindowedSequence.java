package hzt.sequences;

import hzt.collections.ListX;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class WindowedSequence<T> implements Sequence<ListX<T>> {

    private final Sequence<T> upstream;
    private final int size;
    private final int step;
    private final boolean partialWindows;
    private final boolean reuseBuffer;

    public WindowedSequence(Sequence<T> upstream, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        this.upstream = upstream;
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
        this.reuseBuffer = reuseBuffer;
    }

    @NotNull
    @Override
    public Iterator<ListX<T>> iterator() {
        return new WindowedIterator<>(upstream, size, step, partialWindows,reuseBuffer);
    }
}
