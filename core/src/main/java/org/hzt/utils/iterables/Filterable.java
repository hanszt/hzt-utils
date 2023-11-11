package org.hzt.utils.iterables;

import org.hzt.utils.function.IndexedPredicate;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Filterable<T> extends IndexedIterable<T> {

    Filterable<T> filter(Predicate<? super T> predicate);

    <R> Filterable<T> filterBy(Function<? super T, ? extends R> selector, Predicate<? super R> predicate);

    Filterable<T> filterNot(Predicate<? super T> predicate);

    Filterable<T> filterIndexed(IndexedPredicate<? super T> predicate);
}
