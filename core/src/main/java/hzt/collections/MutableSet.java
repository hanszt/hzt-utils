package hzt.collections;

import java.util.Collection;
import java.util.Set;

public interface MutableSet<E> extends Set<E>, SetView<E>, MutableCollection<E> {

    static <E> MutableSet<E> empty() {
        return new HashSetX<>();
    }

    static <E> MutableSet<E> withInitCapacity(int capacity) {
        return new HashSetX<>(capacity);
    }

    static <E> MutableSet<E> of(Set<E> set) {
        return new HashSetX<>(set);
    }

    static <E> MutableSet<E> of(Iterable<E> set) {
        return new HashSetX<>(set);
    }

    static <E> MutableSet<E> of(Collection<E> collection) {
        return new HashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableSet<E> of(E... values) {
        return new HashSetX<>(values);
    }

    boolean isEmpty();
}
