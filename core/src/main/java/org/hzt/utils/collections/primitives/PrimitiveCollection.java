package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.iterables.Indexable;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;

/**
 * @param <T> The boxed type
 * @param <C> The primitive consumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveCollection<T, C, A> extends Indexable<T> {

    int size();

    boolean isEmpty();

    boolean isNotEmpty();

    boolean containsAll(@NotNull Iterable<T> iterable);

    boolean containsAll(@NotNull A array);

    PrimitiveCollection<T, C, A> plus(Iterable<T> iterable);

    PrimitiveCollection<T, C, A> plus(@NotNull A array);

    PrimitiveCollection<T, C, A> take(long n);

    @NotNull PrimitiveIterator<T, C> iterator();

    @Override
    Spliterator<T> spliterator();

    CollectionX<T> boxed();

    A toArray();
}
