package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

public interface LongMutableSet extends LongMutableCollection {

    static LongMutableSet empty() {
        return new LongHashSet();
    }

    static LongMutableSet of(long @NotNull ... values) {
        return new LongHashSet(values);
    }
}
