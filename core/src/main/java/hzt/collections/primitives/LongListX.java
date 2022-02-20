package hzt.collections.primitives;

import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;

public interface LongListX extends PrimitiveCollectionX<Long, LongConsumer, long[]>, LongSequence {

    static LongListX empty() {
        return new LongArrayList();
    }

    static LongListX of(Iterable<Long> iterable) {
        return new LongArrayList(iterable);
    }

    static LongListX of(LongListX longListX) {
        return new LongArrayList(longListX);
    }

    static LongListX of(long... array) {
        return new LongArrayList(array);
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
    default boolean contains(Long l) {
        return contains((long) l);
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Long> iterable) {
        return LongSequence.of(iterable).all(this::contains);
    }

    default boolean contains(long l) {
        return indexOf(l) >= 0;
    }

    int indexOf(long l);

    @Override
    long[] toArray();

    @Override
    String toString();
}
