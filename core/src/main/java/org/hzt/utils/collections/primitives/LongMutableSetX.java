package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

public interface LongMutableSetX extends LongMutableCollection {

    static LongMutableSetX empty() {
        return new LongHashSet();
    }

    static LongMutableSetX of(long @NotNull ... values) {
        return new LongHashSet(values);
    }
}
