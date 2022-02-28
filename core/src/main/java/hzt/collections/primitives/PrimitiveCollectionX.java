package hzt.collections.primitives;

import hzt.collections.CollectionX;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;

/**
 * @param <T> The boxed type
 * @param <C> The PrimitiveConsumer
 * @param <A> The primitive array Type
 */
public interface PrimitiveCollectionX<T, C, A> {

    int size();

    boolean isEmpty();

    boolean isNotEmpty();

    boolean containsAll(@NotNull Iterable<T> iterable);

    boolean containsAll(@NotNull A array);

    PrimitiveCollectionX<T, C, A> plus(Iterable<T> iterable);

    PrimitiveCollectionX<T, C, A> plus(@NotNull A array);

    PrimitiveIterator<T, C> iterator();

    Spliterator<T> spliterator();

    CollectionX<T> boxed();

    A toArray();
}
