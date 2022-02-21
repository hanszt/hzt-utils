package hzt.collections.primitives;

import hzt.collections.ListX;

public interface IntListX extends IntCollection {

    static IntListX empty() {
        return new IntArrayList();
    }

    static IntListX of(Iterable<Integer> iterable) {
        return new IntArrayList(iterable);
    }

    static IntListX of(IntListX intListX) {
        return new IntArrayList(intListX);
    }

    static IntListX of(int... array) {
        return new IntArrayList(array);
    }

    default boolean contains(int i) {
        return indexOf(i) >= 0;
    }

    int indexOf(int i);

    @Override
    int[] toArray();

    @Override
    default ListX<Integer> boxed() {
        return asSequence().boxed().toListX();
    }
}
