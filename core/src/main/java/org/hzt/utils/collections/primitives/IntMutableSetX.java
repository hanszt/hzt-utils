package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

public interface IntMutableSetX extends IntMutableCollection {

    static IntMutableSetX empty() {
        return new IntHashSet();
    }

    static IntMutableSetX of(int @NotNull ... values) {
        return new IntHashSet(values);
    }
}
