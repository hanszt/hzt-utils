package org.hzt.utils.sequences;

public interface SkipTakeSequence<T> extends Sequence<T> {

    @Override
    Sequence<T> skip(long n);

    @Override
    Sequence<T> take(long n);
}
