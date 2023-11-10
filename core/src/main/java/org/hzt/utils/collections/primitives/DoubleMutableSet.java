package org.hzt.utils.collections.primitives;


public interface DoubleMutableSet extends DoubleMutableCollection {

    static DoubleMutableSet empty() {
        return new DoubleHashSet();
    }

    static DoubleMutableSet of(double... values) {
        return new DoubleHashSet(values);
    }
}
