package org.hzt.utils.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

final class EmptySequence<T> implements SkipTakeSequence<T> {

    @Override
    public Sequence<T> skip(final long n) {
        return new EmptySequence<>();
    }

    @Override
    public Sequence<T> take(final long n) {
        return new EmptySequence<>();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }
}
