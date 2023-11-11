package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.DoubleCollection;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableCollection;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.collections.primitives.DoubleMutableSet;
import org.hzt.utils.collectors.primitves.DoubleCollector;

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
        final var iterator = iterator();
        final var result = supplier.get();
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
        final var result1 = downStream1.supplier().get();
        final var result2 = downStream2.supplier().get();
        final var accumulator1 = downStream1.accumulator();
        final var accumulator2 = downStream2.accumulator();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default DoubleList toList() {
        return DoubleList.copyOf(toMutableList());
    }

    default <C extends DoubleMutableCollection> C to(final Supplier<C> collectionFactory) {
        final var collection = collectionFactory.get();
        final var iterator = iterator();
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
        final var collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfDouble iterable = this;
        if (iterable instanceof DoubleMutableCollection) {
            final var c = (DoubleMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        var count = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends DoubleMutableCollection> C skipTo(final Supplier<C> collectionFactory, final int count) {
        final var collection = collectionFactory.get();
        var counter = 0;
        for (final var iterator = this.iterator(); iterator.hasNext(); ) {
            final var value = iterator.nextDouble();
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }
}
