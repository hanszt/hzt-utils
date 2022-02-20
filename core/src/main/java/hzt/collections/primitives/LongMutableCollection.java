package hzt.collections.primitives;

import java.util.function.LongConsumer;

public interface LongMutableCollection extends PrimitiveMutableCollectionX<Long, LongConsumer, long[]> {
    @Override
    default boolean add(Long l) {
        return add((long) l);
    }

    boolean add(long l);
}
