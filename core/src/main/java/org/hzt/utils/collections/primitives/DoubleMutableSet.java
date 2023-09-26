package org.hzt.utils.collections.primitives;

import org.jetbrains.annotations.NotNull;

public interface DoubleMutableSet extends DoubleMutableCollection {

    static DoubleMutableSet empty() {
        return new DoubleHashSet();
    }

    static DoubleMutableSet of(final double @NotNull ... values) {
        return new DoubleHashSet(values);
    }
}
