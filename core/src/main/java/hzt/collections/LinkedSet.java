package hzt.collections;

import java.util.Collection;
import java.util.Set;

public interface LinkedSet<E> extends SetView<E> {

    static <E> LinkedSet<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> LinkedSet<E> of(Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> LinkedSet<E> of(Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> LinkedSet<E> of(Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> LinkedSet<E> of(E first, E... others) {
        return new LinkedHashSetX<>(first, others);
    }
}
