package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

public interface IntMutableSet extends IntMutableCollection {

    static IntMutableSet empty() {
        return new IntHashSet();
    }

    static IntMutableSet of(final int @NotNull ... values) {
        return new IntHashSet(values);
    }
}
