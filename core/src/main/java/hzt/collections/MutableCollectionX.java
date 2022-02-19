package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Stream;

public interface MutableCollectionX<E> extends Collection<E>, CollectionX<E> {

    int size();

    @Override
    default Stream<E> stream() {
        return Collection.super.stream();
    }

    default boolean isEmpty()  {
        return CollectionX.super.isEmpty();
    }

    boolean contains(Object value);

    boolean containsAll(@NotNull Collection<?> c);

    default boolean removeFirst() {
        return remove(first());
    }

    default boolean removeLast() {
        return remove(last());
    }
}
