package org.hzt.utils.iterables;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface Skipable<T> extends Iterable<T> {

    Skipable<T> skip(long count);

    Skipable<T> skipWhile(@NotNull Predicate<? super T> predicate);

    Skipable<T> skipWhileInclusive(@NotNull Predicate<? super T> predicate);
}
