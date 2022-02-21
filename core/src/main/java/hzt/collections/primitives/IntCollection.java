package hzt.collections.primitives;

import hzt.iterables.primitives.IntCollectable;
import hzt.iterables.primitives.IntNumerable;
import hzt.iterables.primitives.IntReducable;
import hzt.iterables.primitives.IntStreamable;
import hzt.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

public interface IntCollection extends IntReducable, IntCollectable, IntNumerable, IntStreamable,
        PrimitiveCollectionX<Integer, IntConsumer, int[]> {

    default int size() {
        return (int) asSequence().count();
    }

    default boolean isEmpty() {
        return asSequence().none();
    }

    default boolean isNotEmpty() {
        return asSequence().any();
    }

    @Override
    default boolean contains(Integer o) {
        return contains((int) o);
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Integer> iterable) {
        return IntSequence.of(iterable).all(this::contains);
    }

    boolean contains(int i);

    @Override
    default IntListX plus(@NotNull Iterable<Integer> values) {
        return IntListX.of(asSequence().plus(values));
    }

    default IntSequence asSequence() {
        return IntSequence.of(this);
    }

    @Override
    int[] toArray();
}
