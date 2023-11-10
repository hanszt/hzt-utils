package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.DoubleCollection;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableCollection;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.collections.primitives.DoubleMutableSet;
import org.hzt.utils.collectors.primitves.DoubleCollector;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface DoubleCollectable extends PrimitiveCollectable<DoubleCollection>, PrimitiveIterable.OfDouble {

    default <R> R collect(final Supplier<R> supplier,
                          final ObjDoubleConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(final Supplier<? extends A> supplier,
                             final ObjDoubleConsumer<? super A> accumulator,
                             final Function<? super A, ? extends R> finisher) {
        final PrimitiveIterator.OfDouble iterator = iterator();
        final A result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextDouble());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(final DoubleCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            final DoubleCollector<A1, R1> downStream1,
            final DoubleCollector<A2, R2> downStream2,
            final BiFunction<R1, R2, R> combiner) {
        final A1 result1 = downStream1.supplier().get();
        final A2 result2 = downStream2.supplier().get();
        final ObjDoubleConsumer<A1> accumulator1 = downStream1.accumulator();
        final ObjDoubleConsumer<A2> accumulator2 = downStream2.accumulator();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final double value = iterator.nextDouble();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default DoubleList toList() {
        return DoubleList.copyOf(toMutableList());
    }

    default <C extends DoubleMutableCollection> C to(final Supplier<C> collectionFactory) {
        final C collection = collectionFactory.get();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while(iterator.hasNext()) {
            collection.add(iterator.nextDouble());
        }
        return collection;
    }

    default DoubleMutableList toMutableList() {
        return to(DoubleMutableList::empty);
    }

    @Override
    default DoubleMutableSet toMutableSet() {
        return to(DoubleMutableSet::empty);
    }

    default <C extends DoubleMutableCollection> C takeTo(final Supplier<C> collectionFactory, final int n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfDouble iterable = this;
        if (iterable instanceof DoubleMutableCollection) {
            final DoubleMutableCollection c = (DoubleMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        int count = 0;
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final double value = iterator.nextDouble();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends DoubleMutableCollection> C skipTo(final Supplier<C> collectionFactory, final int count) {
        final C collection = collectionFactory.get();
        int counter = 0;
        for (final PrimitiveIterator.OfDouble iterator = this.iterator(); iterator.hasNext(); ) {
            final double value = iterator.nextDouble();
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }
}
