package org.hzt.utils.iterables;

import org.hzt.utils.function.IndexedPredicate;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Filterable<T> extends IndexedIterable<T> {

    Filterable<T> filter(@NotNull Predicate<? super T> predicate);

    <R> Filterable<T> filterBy(@NotNull Function<? super T, ? extends R> selector, @NotNull Predicate<? super R> predicate);

    Filterable<T> filterNot(@NotNull Predicate<? super T> predicate);

    Filterable<T> filterIndexed(@NotNull IndexedPredicate<? super T> predicate);
}
