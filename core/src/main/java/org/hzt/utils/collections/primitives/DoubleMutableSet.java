package org.hzt.utils.collections.primitives;


import org.hzt.utils.collections.MutableSetX;

public interface DoubleMutableSet extends DoubleMutableCollection, DoubleSet {

    static DoubleMutableSet empty() {
        return new DoubleHashSet();
    }

    static DoubleMutableSet of(final double... values) {
        return new DoubleHashSet(values);
    }

    @Override
    default MutableSetX<Double> boxed() {
        return MutableSetX.of(this);
    }
}
