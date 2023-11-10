package org.hzt.utils.iterables;


import java.util.function.Predicate;

public interface Takeable<T> extends Iterable<T> {

    Takeable<T> take(long n);

    Takeable<T> takeWhile(Predicate<? super T> predicate);

    Takeable<T> takeWhileInclusive(Predicate<? super T> predicate);
}
