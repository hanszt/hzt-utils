package org.hzt.utils.collectors;

public interface Collector<T, A, R> {
    A supply();

    void accumulate(A acc, T t);

    R finish(A a);
}
