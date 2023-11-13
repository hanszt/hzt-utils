package org.hzt.utils.collections;

public interface Bag<E> extends CollectionX<E> {

    static <E> Bag<E> empty() {
        return new SimpleBag<>();
    }

    boolean add(E item);

    default boolean addAll(Iterable<E> items) {
        boolean allAdded = true;
        for (E item : items) {
            if (!add(item)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    boolean isEmpty();

    @Override
    int size();
}
