package hzt.collections;

import hzt.utils.Transformable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public interface MutableSetX<E> extends Set<E>, SetX<E>, Transformable<MutableSetX<E>> {

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

    @Override
    default Stream<E> stream() {
        return Set.super.stream();
    }

    @Override
    default MutableSetX<E> get() {
        return this;
    }
}
