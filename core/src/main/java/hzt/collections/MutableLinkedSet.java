package hzt.collections;

import java.util.Collection;
import java.util.Set;

public interface MutableLinkedSet<E> extends MutableSet<E> {

    static <E> MutableLinkedSet<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> MutableLinkedSet<E> withInitCapacity(int capacity) {
        return new LinkedHashSetX<>(capacity);
    }

    static <E> MutableLinkedSet<E> of(Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSet<E> of(Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSet<E> of(Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableLinkedSet<E> of(E first, E... others) {
        return new LinkedHashSetX<>(first, others);
    }

}
