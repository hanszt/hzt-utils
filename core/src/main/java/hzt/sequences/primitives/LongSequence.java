package hzt.sequences.primitives;

import hzt.PreConditions;
import hzt.function.TriFunction;
import hzt.iterables.primitives.LongCollectable;
import hzt.iterables.primitives.LongIterable;
import hzt.iterables.primitives.LongNumerable;
import hzt.iterables.primitives.LongReducable;
import hzt.iterables.primitives.LongStreamable;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.iterators.primitives.LongFilteringIterator;
import hzt.iterators.primitives.LongGeneratorIterator;
import hzt.iterators.primitives.LongMultiMappingIterator;
import hzt.iterators.primitives.LongSkipWhileIterator;
import hzt.iterators.primitives.LongTakeWhileIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.numbers.LongX;
import hzt.sequences.Sequence;
import hzt.sequences.SkipTakeSequence;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ToLongFunction;
import java.util.stream.LongStream;

@FunctionalInterface
public interface LongSequence extends LongWindowedSequence, LongReducable, LongCollectable, LongNumerable, LongStreamable,
        PrimitiveSortable<LongComparator>,
        PrimitiveSequence<Long, LongConsumer, LongUnaryOperator, LongPredicate, LongBinaryOperator> {

    static LongSequence empty() {
        return PrimitiveIterators::emptyLongIterator;
    }

    static LongSequence of(Iterable<Long> iterable) {
        if (iterable instanceof LongIterable) {
            final var longIterable = (LongIterable) iterable;
            return longIterable::iterator;
        }
        return of(iterable, It::asLong);
    }

    static <T> LongSequence of(Iterable<T> iterable, ToLongFunction<T> mapper) {
        return () -> PrimitiveIterators.longIteratorOf(iterable.iterator(), mapper);
    }

    static LongSequence of(long... longs) {
        return () -> PrimitiveIterators.longArrayIterator(longs);
    }

    static LongSequence of(LongStream longStream) {
        return longStream::iterator;
    }

    static LongSequence generate(long seedValue, LongUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static LongSequence generate(@NotNull LongSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsLong());
    }

    static LongSequence generate(@NotNull LongSupplier seedFunction, @NotNull LongUnaryOperator nextFunction) {
        return () -> LongGeneratorIterator.of(seedFunction, nextFunction);
    }

    default LongSequence step(long step) {
        return filter(l -> l % step == 0);
    }

    default LongSequence plus(long @NotNull ... values) {
        return Sequence.of(this, LongSequence.of(values)).flatMap(It::self).mapToLong(It::asLong);
    }

    default LongSequence plus(@NotNull Iterable<Long> values) {
        return Sequence.of(this, LongSequence.of(values)).flatMap(It::self).mapToLong(It::asLong);
    }

    default LongSequence map(@NotNull LongUnaryOperator unaryOperator) {
        return () -> PrimitiveIterators.longTransformingIterator(iterator(), unaryOperator);
    }

    default LongSequence flatMap(LongFunction<? extends LongSequence> flatMapper) {
        return mapMulti((value, c) -> flatMapper.apply(value).forEachLong(c));
    }

    default LongSequence mapMulti(LongMapMultiConsumer longMapMultiConsumer) {
        return () -> LongMultiMappingIterator.of(iterator(), longMapMultiConsumer);
    }

    @FunctionalInterface
    interface LongMapMultiConsumer {
        void accept(long value, LongConsumer lc);
    }

    default IntSequence mapToInt(LongToIntFunction mapper) {
        return () -> PrimitiveIterators.longToIntIterator(iterator(), mapper);
    }

    default DoubleSequence mapToDouble(LongToDoubleFunction mapper) {
        return () -> PrimitiveIterators.longToDoubleIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapToObj(@NotNull LongFunction<R> mapper) {
        return () -> PrimitiveIterators.longToObjIterator(iterator(), mapper);
    }

    default Sequence<Long> boxed() {
        return Sequence.of(this);
    }

    default LongSequence filter(@NotNull LongPredicate predicate) {
        return () -> LongFilteringIterator.of(iterator(), predicate, true);
    }

    default @NotNull LongSequence filterNot(@NotNull LongPredicate predicate) {
        return () -> LongFilteringIterator.of(iterator(), predicate, false);
    }

    default LongSequence take(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return PrimitiveIterators::emptyLongIterator;
        } else if (this instanceof SkipTakeSequence) {
            LongSkipTakeSequence skipTakeSequence = (LongSkipTakeSequence) this;
            return skipTakeSequence.take(n);
        } else {
            return new LongTakeSequence(this, n);
        }
    }

    default LongSequence takeWhile(@NotNull LongPredicate predicate) {
        return () -> LongTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default LongSequence takeWhileInclusive(@NotNull LongPredicate predicate) {
        return () -> LongTakeWhileIterator.of(iterator(), predicate, true);
    }

    default LongSequence skip(long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        if (n == 0) {
            return this;
        } else if (this instanceof LongSkipTakeSequence) {
            LongSkipTakeSequence skipTakeSequence = (LongSkipTakeSequence) this;
            return skipTakeSequence.skip(n);
        } else {
            return new LongSkipSequence(this, n);
        }
    }

    default LongSequence skipWhile(@NotNull LongPredicate longPredicate) {
        return () -> LongSkipWhileIterator.of(iterator(), longPredicate, false);
    }

    @Override
    default LongSequence skipWhileInclusive(@NotNull LongPredicate longPredicate) {
        return () -> LongSkipWhileIterator.of(iterator(), longPredicate, true);
    }

    @Override
    default LongSequence sorted() {
        return toListX().sorted().asSequence();
    }

    default LongSequence sorted(LongComparator longComparator) {
        return toListX().sorted(longComparator).asSequence();
    }

    @Override
    default LongSequence sortedDescending() {
        return sorted(LongX::compareReversed);
    }

    default @NotNull LongSequence onEach(@NotNull LongConsumer consumer) {
        return map(l -> {
            consumer.accept(l);
            return l;
        });
    }

    default LongSequence zip(@NotNull LongBinaryOperator merger, long... array) {
        final var iterator = PrimitiveIterators.longArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default LongSequence zip(@NotNull LongBinaryOperator merger, @NotNull Iterable<Long> other) {
        final var iterator = PrimitiveIterators.longIteratorOf(other.iterator(), It::asLong);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default LongSequence zipWithNext(@NotNull LongBinaryOperator merger) {
        return windowed(2, w -> merger.applyAsLong(w.first(), w.last()));
    }

    default long[] toArray() {
        return toListX().toArray();
    }

    default <R> R transform(@NotNull Function<? super LongSequence, ? extends R> resultMapper) {
        return resultMapper.apply(this);
    }

    default LongSequence onSequence(Consumer<? super LongSequence> consumer) {
        consumer.accept(this);
        return this;
    }

    default <R1, R2, R> R longsToTwo(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                     @NotNull Function<? super LongSequence, ? extends R2> resultMapper2,
                                     @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> longsToTwo(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                             @NotNull Function<? super LongSequence, ? extends R2> resultMapper2) {
        return longsToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R longsToThree(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                           @NotNull Function<? super LongSequence, ? extends R2> resultMapper2,
                                           @NotNull Function<? super LongSequence, ? extends R3> resultMapper3,
                                           @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> longsToThree(@NotNull Function<? super LongSequence, ? extends R1> resultMapper1,
                                                         @NotNull Function<? super LongSequence, ? extends R2> resultMapper2,
                                                         @NotNull Function<? super LongSequence, ? extends R3> resultMapper3) {
        return longsToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }
}
