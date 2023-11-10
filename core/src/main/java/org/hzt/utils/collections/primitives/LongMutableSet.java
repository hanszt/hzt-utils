package org.hzt.utils.collections.primitives;


public interface LongMutableSet extends LongMutableCollection {

    static LongMutableSet empty() {
        return new LongHashSet();
    }

    static LongMutableSet of(long... values) {
        return new LongHashSet(values);
    }
}
