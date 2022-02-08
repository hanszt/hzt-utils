package hzt.collections;

import hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public interface SetX<E> extends CollectionView<E>, Transformable<SetX<E>> {

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

    @Override
    int size();

    @Override
    boolean contains(Object value);

    default @NotNull SetX<E> get() {
        return this;
    }
}
