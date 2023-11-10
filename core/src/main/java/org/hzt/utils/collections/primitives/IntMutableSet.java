package org.hzt.utils.collections.primitives;


public interface IntMutableSet extends IntMutableCollection {

    static IntMutableSet empty() {
        return new IntHashSet();
    }

    static IntMutableSet of(int... values) {
        return new IntHashSet(values);
    }
}
