package hzt.sequences.primitives;

import hzt.function.TriFunction;
import hzt.iterables.primitives.DoubleCollectable;
import hzt.iterables.primitives.DoubleIterable;
import hzt.iterables.primitives.DoubleNumerable;
import hzt.iterables.primitives.DoubleReducable;
import hzt.iterables.primitives.DoubleStreamable;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.iterators.primitives.DoubleFilteringIterator;
import hzt.iterators.primitives.DoubleGeneratorIterator;
import hzt.iterators.primitives.DoubleMultiMappingIterator;
import hzt.iterators.primitives.DoubleSkipWhileIterator;
import hzt.iterators.primitives.DoubleTakeWhileIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

@FunctionalInterface
public interface DoubleSequence extends DoubleWindowedSequence, DoubleReducable, DoubleCollectable, DoubleNumerable, DoubleStreamable,
        PrimitiveSortable<DoubleComparator>,
        PrimitiveSequence<Double, DoubleConsumer, DoubleUnaryOperator, DoublePredicate, DoubleBinaryOperator> {

    static DoubleSequence empty() {
        return PrimitiveIterators::emptyDoubleIterator;
    }

    static DoubleSequence of(Iterable<Double> iterable) {
        if (iterable instanceof DoubleIterable) {
            final var doubleIterable = (DoubleIterable) iterable;
            return doubleIterable::iterator;
        }
        return of(iterable, It::asDouble);
    }

    static <T> DoubleSequence of(Iterable<T> iterable, ToDoubleFunction<T> mapper) {
        return () -> PrimitiveIterators.doubleIteratorOf(iterable.iterator(), mapper);
    }

    static DoubleSequence of(double... doubles) {
        return () -> PrimitiveIterators.doubleArrayIterator(doubles);
    }

    static DoubleSequence of(DoubleStream stream) {
        return stream::iterator;
    }

    static DoubleSequence generate(double seedValue, DoubleUnaryOperator nextFunction) {
        return generate(() -> seedValue, nextFunction);
    }

    static DoubleSequence generate(@NotNull DoubleSupplier nextFunction) {
        return generate(nextFunction, t -> nextFunction.getAsDouble());
    }

    static DoubleSequence generate(@NotNull DoubleSupplier seedFunction, @NotNull DoubleUnaryOperator nextFunction) {
        return () -> DoubleGeneratorIterator.of(seedFunction, nextFunction);
    }

    default DoubleSequence plus(double @NotNull ... values) {
        return Sequence.of(this, DoubleSequence.of(values)).flatMap(It::self).mapToDouble(It::asDouble);
    }

    default DoubleSequence plus(@NotNull Iterable<Double> values) {
        return Sequence.of(this, DoubleSequence.of(values)).flatMap(It::self).mapToDouble(It::asDouble);
    }

    @Override
    default DoubleSequence map(@NotNull DoubleUnaryOperator mapper) {
        return () -> PrimitiveIterators.doubleTransformingIterator(iterator(), mapper);
    }

    default DoubleSequence flatMap(DoubleFunction<? extends DoubleSequence> flatMapper) {
        return mapMulti((value, c) -> flatMapper.apply(value).forEachDouble(c));
    }

    default DoubleSequence mapMulti(DoubleMapMultiConsumer mapMultiConsumer) {
        return () -> DoubleMultiMappingIterator.of(iterator(), mapMultiConsumer);
    }

    @FunctionalInterface
    interface DoubleMapMultiConsumer {
        void accept(double value, DoubleConsumer intConsumer);
    }

    default IntSequence mapToInt(DoubleToIntFunction mapper) {
        return () -> PrimitiveIterators.doubleToIntIterator(iterator(), mapper);
    }

    default LongSequence mapToLong(DoubleToLongFunction mapper) {
        return () -> PrimitiveIterators.doubleToLongIterator(iterator(), mapper);
    }

    default <R> Sequence<R> mapToObj(@NotNull DoubleFunction<R> mapper) {
        return () -> PrimitiveIterators.doubleToObjIterator(iterator(), mapper);
    }

    @Override
    default Sequence<Double> boxed() {
        return Sequence.of(this);
    }

    @Override
    default DoubleSequence take(long n) {
        return new DoubleTakeSequence(this, n);
    }

    @Override
    default DoubleSequence takeWhile(@NotNull DoublePredicate predicate) {
        return () -> DoubleTakeWhileIterator.of(iterator(), predicate);
    }

    @Override
    default DoubleSequence takeWhileInclusive(@NotNull DoublePredicate predicate) {
        return () -> DoubleTakeWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence skip(long n) {
        return new DoubleSkipSequence(this, n);
    }

    @Override
    default DoubleSequence skipWhile(@NotNull DoublePredicate predicate) {
        return () -> DoubleSkipWhileIterator.of(iterator(), predicate, false);
    }

    @Override
    default DoubleSequence skipWhileInclusive(@NotNull DoublePredicate predicate) {
        return () -> DoubleSkipWhileIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence sorted() {
        return toListX().sorted().asSequence();
    }

    default DoubleSequence sorted(DoubleComparator comparator) {
        return toListX().sorted(comparator).asSequence();
    }

    @Override
    default DoubleSequence sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }

    @Override
    default @NotNull DoubleSequence filter(@NotNull DoublePredicate predicate) {
        return () -> DoubleFilteringIterator.of(iterator(), predicate, true);
    }

    @Override
    default DoubleSequence filterNot(@NotNull DoublePredicate predicate) {
        return () -> DoubleFilteringIterator.of(iterator(), predicate, false);
    }

    @Override
    @NotNull
    default DoubleSequence onEach(@NotNull DoubleConsumer consumer) {
        return map(d -> {
            consumer.accept(d);
            return d;
        });
    }

    default DoubleSequence zip(@NotNull DoubleBinaryOperator merger, double... array) {
        final var iterator = PrimitiveIterators.doubleArrayIterator(array);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default DoubleSequence zip(@NotNull DoubleBinaryOperator merger, @NotNull Iterable<Double> other) {
        final var iterator = PrimitiveIterators.doubleIteratorOf(other.iterator(), It::asDouble);
        return () -> PrimitiveIterators.mergingIterator(iterator(), iterator, merger);
    }

    @Override
    default DoubleSequence zipWithNext(@NotNull DoubleBinaryOperator merger) {
        return windowed(2, s -> merger.applyAsDouble(s.first(), s.last()));
    }

    default double[] toArray() {
        return toListX().toArray();
    }

    default <R1, R2, R> R doublesToTwo(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                       @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                       @NotNull BiFunction<R1, R2, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this));
    }

    default <R1, R2> Pair<R1, R2> doublesToTwo(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                               @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2) {
        return doublesToTwo(resultMapper1, resultMapper2, Pair::of);
    }

    default <R1, R2, R3, R> R doublesToThree(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                             @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                             @NotNull Function<? super DoubleSequence, ? extends R3> resultMapper3,
                                             @NotNull TriFunction<R1, R2, R3, R> merger) {
        return merger.apply(resultMapper1.apply(this), resultMapper2.apply(this), resultMapper3.apply(this));
    }

    default <R1, R2, R3> Triple<R1, R2, R3> doublesToThree(@NotNull Function<? super DoubleSequence, ? extends R1> resultMapper1,
                                                           @NotNull Function<? super DoubleSequence, ? extends R2> resultMapper2,
                                                           @NotNull Function<? super DoubleSequence, ? extends R3> resultMapper3) {
        return doublesToThree(resultMapper1, resultMapper2, resultMapper3, Triple::of);
    }
}
