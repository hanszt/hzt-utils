package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.DoubleCollection;
import org.hzt.utils.collections.primitives.DoubleListX;
import org.hzt.utils.collections.primitives.DoubleMutableCollection;
import org.hzt.utils.collections.primitives.DoubleMutableListX;
import org.hzt.utils.collectors.primitves.DoubleCollector;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface DoubleCollectable extends PrimitiveIterable.OfDouble, PrimitiveCollectable<DoubleCollection> {

    default <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(Supplier<A> supplier, ObjDoubleConsumer<A> accumulator, Function<A, R> finisher) {
        PrimitiveIterator.OfDouble iterator = iterator();
        final var result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextDouble());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(DoubleCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull DoubleCollector<A1, R1> downStream1,
            @NotNull DoubleCollector<A2, R2> downStream2,
            @NotNull BiFunction<R1, R2, R> combiner) {
        final var result1 = downStream1.supplier().get();
        final var result2 = downStream2.supplier().get();
        final var accumulator1 = downStream1.accumulator();
        final var accumulator2 = downStream2.accumulator();
        PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default DoubleListX toListX() {
        return toMutableList();
    }

    default <C extends DoubleMutableCollection> C to(Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        final var iterator = iterator();
        while(iterator.hasNext()) {
            collection.add(iterator.nextDouble());
        }
        return collection;
    }

    default DoubleMutableListX toMutableList() {
        return to(DoubleMutableListX::empty);
    }

    default <C extends DoubleMutableCollection> C takeTo(Supplier<C> collectionFactory, long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfDouble iterable = this;
        if (iterable instanceof DoubleMutableCollection c && n >= c.size()) {
            collection.addAll(c);
            return collection;
        }
        int count = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            double value = iterator.nextDouble();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }
}
