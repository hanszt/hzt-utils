package hzt.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public interface MutableLinkedSetX<E> extends MutableSetX<E> {

    static <E> MutableLinkedSetX<E> empty() {
        return new LinkedHashSetX<>();
    }

    static <E> MutableLinkedSetX<E> of(Set<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSetX<E> of(Iterable<E> set) {
        return new LinkedHashSetX<>(set);
    }

    static <E> MutableLinkedSetX<E> of(Collection<E> collection) {
        return new LinkedHashSetX<>(collection);
    }

    @SafeVarargs
    static <E> MutableLinkedSetX<E> of(E first, E... others) {
        return new LinkedHashSetX<>(first, others);
    }

    @NotNull
    @Override
    default Iterator<E> iterator() {
        return iterable().iterator();
    }

    @Override
    default Stream<E> stream() {
        return MutableSetX.super.stream();
    }

}
