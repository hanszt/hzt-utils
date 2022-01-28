package hzt.collections;

import hzt.function.It;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public interface SetX<E> extends CollectionView<E> {

    static <E> SetX<E> empty() {
        return new HashSetX<>();
    }

    static <E> SetX<E> of(Iterable<E> iterable) {
        return new HashSetX<>(iterable);
    }

    @SafeVarargs
    static <E> SetX<E> of(E... values) {
        MutableSetX<E> resultSet = MutableSetX.of(values);
        resultSet.addAll(Arrays.asList(values));
        return resultSet;
    }

    static <E> SetX<E> copyOf(Iterable<E> iterable) {
        return new HashSetX<>(iterable);
    }

    default Set<E> toSet() {
        return toSetOf(It::self);
    }

    int size();

    boolean contains(Object o);

    boolean containsAll(@NotNull Collection<?> c);

    default MutableListX<E> toMutableList() {
        return CollectionView.super.getListOrElseCompute();
    }
}
