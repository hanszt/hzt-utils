package hzt.collections;

import java.util.Collection;
import java.util.Set;

public interface LinkedSetX<E> extends SetX<E> {

    static <E> LinkedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> LinkedSetX<E> of(Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> LinkedSetX<E> of(Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> LinkedSetX<E> of(Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> LinkedSetX<E> of(E first, E... others) {
        return new LinkedHashSetX<>(first, others);
    }
}
