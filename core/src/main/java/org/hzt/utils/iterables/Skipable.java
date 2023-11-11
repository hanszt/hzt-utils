package org.hzt.utils.iterables;


import java.util.function.Predicate;

public interface Skipable<T> extends Iterable<T> {

    Skipable<T> skip(long count);

    Skipable<T> skipWhile(Predicate<? super T> predicate);

    Skipable<T> skipWhileInclusive(Predicate<? super T> predicate);
}
