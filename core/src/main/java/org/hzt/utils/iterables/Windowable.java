package org.hzt.utils.iterables;

import org.hzt.utils.collections.ListX;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("squid:S1452")
public interface Windowable<T> extends Iterable<T> {

    Windowable<? extends Iterable<T>> chunked(int size);

    Windowable<? extends Iterable<T>> windowed(int size);

    <R> Windowable<R> windowed(int size, @NotNull Function<? super ListX<T>, ? extends R> transform);

    Windowable<? extends Iterable<T>> windowed(int size, int step);

    <R> Windowable<R> windowed(int size, int step, @NotNull Function<? super ListX<T>, ? extends R> transform);

    Windowable<? extends Iterable<T>> windowed(int size, boolean partialWindows);

    Windowable<? extends Iterable<T>> windowed(int size, int step, boolean partialWindows);

    <R> Windowable<R> windowed(int size, int step, boolean partialWindows, @NotNull Function<? super ListX<T>, R> transform);
}
