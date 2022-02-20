package hzt.collections.primitives;

import java.util.function.DoubleConsumer;

public interface DoubleMutableCollection extends PrimitiveMutableCollectionX<Double, DoubleConsumer, double[]> {
    @Override
    default boolean add(Double d) {
        return add((double) d);
    }

    boolean add(double d);
}
