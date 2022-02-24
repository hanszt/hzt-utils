package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface MutableListX<E> extends List<E>, ListX<E>, MutableCollectionX<E> {

    static <E> MutableListX<E> empty() {
        return new ArrayListX<>();
    }

    static <E> MutableListX<E> withInitCapacity(int capacity) {
        return new ArrayListX<>(capacity);
    }

    static <E> MutableListX<E> of(@NotNull Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> MutableListX<E> of(@NotNull Collection<E> collection) {
        return new ArrayListX<>(collection);
    }

    @SafeVarargs
    static <E> MutableListX<E> of(@NotNull E... values) {
        return new ArrayListX<>(values);
    }

    static <E> MutableListX<E> of(@NotNull E value) {
        return new ArrayListX<>(value);
    }

    MutableListX<E> headTo(int toIndex);

    MutableListX<E> tailFrom(int fromIndex);

    MutableListX<E> subList(int fromIndex, int toIndex);

    @Override
    int size();

    @Override
    default boolean containsAll(@NotNull Iterable<E> iterable) {
        return ListX.super.containsAll(iterable);
    }

    @Override
    boolean contains(Object o);

    @Override
    boolean isEmpty();

    @Override
    default boolean removeFirst() {
        return remove(get(0));
    }

    @Override
    default boolean removeLast() {
        return remove(get(lastIndex()));
    }

    @Override
    default @NotNull MutableListX<E> get() {
        return this;
    }
}
