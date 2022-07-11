package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.iterables.Indexable;
import org.hzt.utils.iterables.Stringable;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;

/**
 * @param <T> The boxed type
 * @param <C> The PrimitiveConsumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveCollectionX<T, C, A> extends Stringable<T>, Indexable<T> {

    int size();

    boolean isEmpty();

    boolean isNotEmpty();

    boolean containsAll(@NotNull Iterable<T> iterable);

    boolean containsAll(@NotNull A array);

    PrimitiveCollectionX<T, C, A> plus(Iterable<T> iterable);

    PrimitiveCollectionX<T, C, A> plus(@NotNull A array);

    PrimitiveCollectionX<T, C, A> take(long n);

    @NotNull PrimitiveIterator<T, C> iterator();

    @Override
    Spliterator<T> spliterator();

    CollectionX<T> boxed();

    A toArray();
}
