package hzt.collections.primitives;

import java.util.function.IntConsumer;

public interface IntMutableCollection extends PrimitiveMutableCollectionX<Integer, IntConsumer, int[]> {
    @Override
    default boolean add(Integer i) {
        return add((int) i);
    }

    boolean add(int i);
}
