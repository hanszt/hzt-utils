package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;

public interface LongMutableCollection extends PrimitiveMutableCollectionX<Long, LongConsumer, long[]>, LongCollection {
    @Override
    default boolean add(Long l) {
        return add((long) l);
    }

    @Override
    default boolean addAll(Iterable<Long> iterable) {
        final var iterator = iterator();
        while (iterator.hasNext()) {
            add(iterator.nextLong());
        }
        return true;
    }

    @Override
    default LongMutableListX plus(@NotNull Iterable<Long> iterable) {
        return LongSequence.of(this).plus(iterable).toMutableList();
    }

    @Override
    MutableCollectionX<Long> boxed();

    boolean add(long l);

    @Override
    long[] toArray();
}
