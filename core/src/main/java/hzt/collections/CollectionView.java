package hzt.collections;

import hzt.iterables.IterableX;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

public interface CollectionView<E> extends IterableX<E> {

    default int size() {
        return count(It.noFilter());
    }

    default boolean isEmpty() {
        return size() > 0;
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default boolean contains(E value) {
        return any(item -> item.equals(value));
    }

    default boolean containsNot(E e) {
        return !contains(e);
    }

    default boolean containsAll(@NotNull Iterable<E> iterable) {
        return Sequence.of(iterable).all(this::contains);
    }

    default boolean containsNoneOf(@NotNull Iterable<E> iterable) {
        return Sequence.of(iterable).none(this::contains);
    }
}
