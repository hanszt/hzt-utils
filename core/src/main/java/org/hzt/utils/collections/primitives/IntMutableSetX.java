package org.hzt.utils.collections.primitives;

public interface IntMutableSetX extends IntMutableCollection {

    static IntMutableSetX empty() {
        return new IntHashSet();
    }
}
