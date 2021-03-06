package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Windowable<T> extends Iterable<T> {

    Windowable<ListX<T>> chunked(int size);

    Windowable<ListX<T>> windowed(int size);

    <R> Windowable<R> windowed(int size, @NotNull Function<? super ListX<T>, ? extends R> transform);

    Windowable<ListX<T>> windowed(int size, int step);

    <R> Windowable<R> windowed(int size, int step, @NotNull Function<? super ListX<T>, ? extends R> transform);

    Windowable<ListX<T>> windowed(int size, boolean partialWindows);

    Windowable<ListX<T>> windowed(int size, int step, boolean partialWindows);

    <R> Windowable<R> windowed(int size, int step, boolean partialWindows, @NotNull Function<? super ListX<T>, R> transform);
}
