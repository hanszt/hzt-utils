package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

public interface IntMutableCollection extends PrimitiveMutableCollectionX<Integer, IntConsumer, int[]>, IntCollection {
    @Override
    default boolean add(Integer i) {
        return add((int) i);
    }

    boolean add(int i);

    @Override
    default boolean addAll(Iterable<Integer> iterable) {
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            add(iterator.nextInt());
        }
        return true;
    }

    @Override
    default IntMutableListX plus(@NotNull Iterable<Integer> iterable) {
        return IntSequence.of(this).plus(iterable).toMutableList();
    }

    @Override
    MutableCollectionX<Integer> boxed();

    @Override
    int[] toArray();
}
