package org.hzt.utils.iterables;


import java.util.function.Function;

public interface Distinctable<T> extends Iterable<T> {

    Distinctable<T> distinct();

    <R> Distinctable<T> distinctBy(Function<? super T, ? extends R> selector);
}
