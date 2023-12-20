package org.hzt.utils.iterables;


import java.util.function.Predicate;

public interface Skippable<T> extends Iterable<T> {

    Skippable<T> skip(long count);

    Skippable<T> skipWhile(Predicate<? super T> predicate);

    Skippable<T> skipWhileInclusive(Predicate<? super T> predicate);
}
