package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface MutableList<E> extends List<E>, ListView<E>, MutableCollection<E> {

    static <E> MutableList<E> empty() {
        return new ArrayListX<>();
    }

    static <E> MutableList<E> withInitCapacity(int capacity) {
        return new ArrayListX<>(capacity);
    }

    static <E> MutableList<E> of(@NotNull Iterable<E> iterable) {
        return new ArrayListX<>(iterable);
    }

    static <E> MutableList<E> of(@NotNull Collection<E> collection) {
        return new ArrayListX<>(collection);
    }

    @SafeVarargs
    static <E> MutableList<E> of(@NotNull E... values) {
        return new ArrayListX<>(values);
    }

    static <E> MutableList<E> of(@NotNull E value) {
        return new ArrayListX<>(value);
    }

    MutableList<E> headTo(int toIndex);

    MutableList<E> tailFrom(int fromIndex);

    MutableList<E> subList(int fromIndex, int toIndex);

    @Override
    int size();

    @Override
    default boolean containsAll(@NotNull Iterable<E> iterable) {
        return ListView.super.containsAll(iterable);
    }

    @Override
    boolean contains(Object o);

    @Override
    boolean isEmpty();

    @Override
    default @NotNull MutableList<E> get() {
        return this;
    }
}
