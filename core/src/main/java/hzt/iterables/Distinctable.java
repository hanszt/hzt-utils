package hzt.iterables;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Distinctable<T> {

    Distinctable<T> distinct();

    <R> Distinctable<T> distinctBy(@NotNull Function<T, ? extends R> selector);
}
