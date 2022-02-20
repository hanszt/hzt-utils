package hzt.collections.primitives;

import hzt.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

public interface IntListX extends PrimitiveCollectionX<Integer, IntConsumer, int[]>, IntSequence {

    static IntListX empty() {
        return new IntArrayList();
    }

    static IntListX of(Iterable<Integer> iterable) {
        return new IntArrayList(iterable);
    }

    static IntListX of(IntListX intListX) {
        return new IntArrayList(intListX);
    }

    static IntListX of(int... array) {
        return new IntArrayList(array);
    }

    default int size() {
        return (int) count();
    }

    default boolean isEmpty() {
        return none();
    }

    default boolean isNotEmpty() {
        return any();
    }

    @Override
    default boolean contains(Integer o) {
        return contains((int) o);
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Integer> iterable) {
        return IntSequence.of(iterable).all(this::contains);
    }

    default boolean contains(int i) {
        return indexOf(i) >= 0;
    }

    int indexOf(int i);

    @Override
    int[] toArray();

    @Override
    String toString();
}
