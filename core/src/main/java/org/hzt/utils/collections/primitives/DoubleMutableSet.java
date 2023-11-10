package org.hzt.utils.collections.primitives;


public interface DoubleMutableSet extends DoubleMutableCollection {

    static DoubleMutableSet empty() {
        return new DoubleHashSet();
    }

    static DoubleMutableSet of(final double... values) {
        return new DoubleHashSet(values);
    }
}
