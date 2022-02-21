package hzt.collections.primitives;

import hzt.collections.ListX;

public interface LongListX extends LongCollection {

    static LongListX empty() {
        return new LongArrayList();
    }

    static LongListX of(Iterable<Long> iterable) {
        return new LongArrayList(iterable);
    }

    static LongListX of(LongListX longListX) {
        return new LongArrayList(longListX);
    }

    static LongListX of(long... array) {
        return new LongArrayList(array);
    }

    default boolean contains(long l) {
        return indexOf(l) >= 0;
    }

    int indexOf(long l);

    @Override
    long[] toArray();

    @Override
    default ListX<Long> boxed() {
        return asSequence().boxed().toListX();
    }
}
