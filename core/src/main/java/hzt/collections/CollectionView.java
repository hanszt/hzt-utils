package hzt.collections;

import hzt.iterables.IterableX;

public interface CollectionView<T> extends IterableX<T> {

    boolean isNotEmpty();

    boolean containsNot(T t);
}
