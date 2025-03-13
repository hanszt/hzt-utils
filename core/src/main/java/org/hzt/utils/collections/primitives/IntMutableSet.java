package org.hzt.utils.collections.primitives;


public interface IntMutableSet extends IntMutableCollection, IntSet {

    static IntMutableSet empty() {
        return new IntHashSet();
    }

    static IntMutableSet of(final int... values) {
        return new IntHashSet(values);
    }
}
