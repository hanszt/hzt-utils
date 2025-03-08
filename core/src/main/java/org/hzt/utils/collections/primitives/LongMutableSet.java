package org.hzt.utils.collections.primitives;


public interface LongMutableSet extends LongMutableCollection, LongSet {

    static LongMutableSet empty() {
        return new LongHashSet();
    }

    static LongMutableSet of(final long... values) {
        return new LongHashSet(values);
    }
}
