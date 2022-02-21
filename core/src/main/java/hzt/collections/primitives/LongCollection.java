package hzt.collections.primitives;

import hzt.iterables.primitives.LongCollectable;
import hzt.iterables.primitives.LongNumerable;
import hzt.iterables.primitives.LongReducable;
import hzt.iterables.primitives.LongStreamable;
import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;

public interface LongCollection extends LongReducable, LongCollectable, LongNumerable, LongStreamable,
        PrimitiveCollectionX<Long, LongConsumer, long[]> {

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
    default boolean contains(Long l) {
        return contains((long) l);
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Long> iterable) {
        return LongSequence.of(iterable).all(this::contains);
    }

    boolean contains(long l);

    @Override
    default LongListX plus(@NotNull Iterable<Long> values) {
        return LongListX.of(asSequence().plus(values));
    }

    default LongSequence asSequence() {
        return LongSequence.of(this);
    }

    @Override
    long[] toArray();
}
