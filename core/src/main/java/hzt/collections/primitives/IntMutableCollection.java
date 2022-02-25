package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.iterables.primitives.IntIterable;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

public interface IntMutableCollection extends PrimitiveMutableCollectionX<Integer, IntConsumer, int[]>, IntCollection {

    boolean add(int i);

    @Override
    default boolean addAll(@NotNull Iterable<Integer> iterable) {
        if (iterable instanceof IntIterable) {
            final var iterator = ((IntIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextInt());
                if (!added) {
                    return false;
                }
            }
            return true;
        }
        for (int i : iterable) {
            final var added = add(i);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean addAll(int @NotNull ... array) {
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        while (iterator.hasNext()) {
            final var added = add(iterator.nextInt());
            if (!added) {
                return false;
            }
        }
        return true;
    }

    boolean remove(int i);

    @Override
    default boolean removeAll(@NotNull Iterable<Integer> iterable) {
        if (iterable instanceof IntIterable) {
            final var iterator = ((IntIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final var removed = remove(iterator.nextInt());
                if (!removed) {
                    return false;
                }
            }
            return true;
        }
        for (int i : iterable) {
            final var removed = remove(i);
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(int @NotNull ... array) {
        final PrimitiveIterator.OfInt iterator = PrimitiveIterators.intArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextInt());
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default IntMutableListX plus(@NotNull Iterable<Integer> iterable) {
        return IntSequence.of(this).plus(iterable).toMutableList();
    }

    @Override
    default IntMutableListX plus(int @NotNull ... array) {
        return IntSequence.of(this).plus(array).toMutableList();
    }

    @Override
    MutableCollectionX<Integer> boxed();

    @Override
    int[] toArray();
}
