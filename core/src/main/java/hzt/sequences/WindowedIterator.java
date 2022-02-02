package hzt.sequences;

import hzt.collections.ListX;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class WindowedIterator<T> implements Iterator<ListX<T>> {

    private final Iterator<T> iterator;
    private final int size;
    private final int step;
    private final boolean partialWindows;
    private final boolean reuseBuffer;

    public WindowedIterator(
            @NotNull Sequence<T> upstream,
            int size,
            int step,
            boolean partialWindows,
            boolean reuseBuffer) {
        this.iterator = upstream.iterator();
        this.size = size;
        this.step = step;
        this.partialWindows = partialWindows;
        this.reuseBuffer = reuseBuffer;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public ListX<T> next() {
        throw new NoSuchElementException("Not yet implemented");
    }
}
