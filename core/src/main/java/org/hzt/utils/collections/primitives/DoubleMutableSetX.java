package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

public interface DoubleMutableSetX extends DoubleMutableCollection {

    static DoubleMutableSetX empty() {
        return new DoubleHashSet();
    }

    static DoubleMutableSetX of(double @NotNull ... values) {
        return new DoubleHashSet(values);
    }
}
