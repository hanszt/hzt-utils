package hzt.collections.primitives;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.iterables.primitives.LongCollectable;
import hzt.iterables.primitives.LongNumerable;
import hzt.iterables.primitives.LongReducable;
import hzt.iterables.primitives.LongStreamable;
import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

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

    @Override
    default boolean containsAll(long @NotNull ... array) {
        return LongSequence.of(array).all(this::contains);
    }

    boolean contains(long l);

    default LongListX filter(LongPredicate predicate) {
        LongMutableListX doubles = LongMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextLong();
            if (predicate.test(value)) {
                doubles.add(value);
            }
        }
        return doubles;
    }

    default LongListX map(LongUnaryOperator mapper) {
        LongMutableListX doubles = LongMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsLong(iterator.nextLong()));
        }
        return doubles;
    }

    default IntListX mapToInt(LongToIntFunction mapper) {
        IntMutableListX ints = IntMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextLong()));
        }
        return ints;
    }

    default DoubleListX mapToDouble(LongToDoubleFunction mapper) {
        DoubleMutableListX doubles = DoubleMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextLong()));
        }
        return doubles;
    }

    default <R> ListX<R> mapToObj(LongFunction<R> mapper) {
        MutableListX<R> doubles = MutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.apply(iterator.nextLong()));
        }
        return doubles;
    }

    @Override
    default LongListX plus(@NotNull Iterable<Long> values) {
        return LongListX.of(asSequence().plus(values));
    }

    @Override
    default LongListX plus(long @NotNull ... array) {
        return asSequence().plus(array).toListX();
    }

    default LongSequence asSequence() {
        return LongSequence.of(this);
    }

    @Override
    long[] toArray();
}
