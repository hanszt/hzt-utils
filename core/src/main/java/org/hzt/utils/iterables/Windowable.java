package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;

import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("squid:S1452")
public interface Windowable<T> extends Iterable<T> {

    Windowable<? extends Iterable<T>> chunked(int size);

    Windowable<? extends Iterable<T>> nextChunkedIf(final Predicate<T> predicate);

    <R> Windowable<R> nextChunkedIf(final Predicate<T> predicate, final Function<ListX<T>, R> transform);

    Windowable<? extends Iterable<T>> windowed(int size);

    <R> Windowable<R> windowed(int size, Function<? super ListX<T>, ? extends R> transform);

    Windowable<? extends Iterable<T>> windowed(int size, int step);

    <R> Windowable<R> windowed(int size, int step, Function<? super ListX<T>, ? extends R> transform);

    Windowable<? extends Iterable<T>> windowed(int size, boolean partialWindows);

    Windowable<? extends Iterable<T>> windowed(int size, int step, boolean partialWindows);

    <R> Windowable<R> windowed(int size, int step, boolean partialWindows, Function<? super ListX<T>, R> transform);
}
