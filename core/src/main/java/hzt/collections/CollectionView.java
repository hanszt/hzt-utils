package hzt.collections;

public interface CollectionView<T> extends IterableX<T> {

    boolean isNotEmpty();

    boolean containsNot(T t);
}
