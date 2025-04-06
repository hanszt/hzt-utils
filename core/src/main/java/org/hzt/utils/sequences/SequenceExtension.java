package org.hzt.utils.sequences;

public interface SequenceExtension<T, R> {

    Sequence<R> extend(Sequence<T> sequence);
}
