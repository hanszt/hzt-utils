package hzt.iterables;

import hzt.collections.ListView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Windowable<T> extends Iterable<T> {

    Windowable<ListView<T>> chunked(int size);

    <R> Windowable<R> chunked(int size, @NotNull Function<? super ListView<T>, ? extends R> transform);

    Windowable<ListView<T>> windowed(int size);

    <R> Windowable<R> windowed(int size, @NotNull Function<? super ListView<T>, ? extends R> transform);

    Windowable<ListView<T>> windowed(int size, int step);

    <R> Windowable<R> windowed(int size, int step, @NotNull Function<? super ListView<T>, ? extends R> transform);

    Windowable<ListView<T>> windowed(int size, boolean partialWindows);

    Windowable<ListView<T>> windowed(int size, int step, boolean partialWindows);

    <R> Windowable<R> windowed(int size, int step, boolean partialWindows, @NotNull Function<? super ListView<T>, R> transform);
}
