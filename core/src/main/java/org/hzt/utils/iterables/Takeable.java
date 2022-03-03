package org.hzt.utils.iterables;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface Takeable<T> extends Iterable<T> {

    Takeable<T> take(long n);

    Takeable<T> takeWhile(@NotNull Predicate<T> predicate);

    Takeable<T> takeWhileInclusive(@NotNull Predicate<T> predicate);
}
