package hzt.collections;

import java.util.Collection;
import java.util.Set;
import java.util.Spliterator;

public interface MutableSetX<E> extends Set<E>, SetX<E>, MutableCollectionX<E> {

    static <E> MutableSetX<E> empty() {
        return new HashSetX<>();
    }

    static <E> MutableSetX<E> withInitCapacity(int capacity) {
        return new HashSetX<>(capacity);
    }

    static <E> MutableSetX<E> of(Set<E> set) {
        return new HashSetX<>(set);
    }

    static <E> MutableSetX<E> of(Iterable<E> set) {
        return new HashSetX<>(set);
    }

    static <E> MutableSetX<E> of(Collection<E> collection) {
        return new HashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableSetX<E> of(E... values) {
        return new HashSetX<>(values);
    }

    boolean isEmpty();

    @Override
    default Spliterator<E> spliterator() {
        return Set.super.spliterator();
    }
}
