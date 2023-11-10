package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.IntCollection;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableCollection;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.collections.primitives.IntMutableSet;
import org.hzt.utils.collectors.primitves.IntCollector;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface IntCollectable extends PrimitiveCollectable<IntCollection>, PrimitiveIterable.OfInt {

    default <R> R collect(final Supplier<R> supplier,
                          final ObjIntConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(final Supplier<A> supplier,
                             final ObjIntConsumer<A> accumulator,
                             final Function<? super A, ? extends R> finisher) {
        final PrimitiveIterator.OfInt iterator = iterator();
        final A result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextInt());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(final IntCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            final IntCollector<A1, ? extends R1> downStream1,
            final IntCollector<A2, ? extends R2> downStream2,
            final BiFunction<? super R1, ? super R2, ? extends R> combiner) {
        final A1 result1 = downStream1.supplier().get();
        final A2 result2 = downStream2.supplier().get();
        final ObjIntConsumer<A1> accumulator1 = downStream1.accumulator();
        final ObjIntConsumer<A2> accumulator2 = downStream2.accumulator();
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final int value = iterator.nextInt();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default IntList toList() {
        return IntList.copyOf(toMutableList());
    }

    default <C extends IntMutableCollection> C to(final Supplier<C> collectionFactory) {
        final C collection = collectionFactory.get();
        final PrimitiveIterator.OfInt iterator = iterator();
        while(iterator.hasNext()) {
            collection.add(iterator.nextInt());
        }
        return collection;
    }

    default IntMutableList toMutableList() {
        return to(IntMutableList::empty);
    }

    @Override
    default IntMutableSet toMutableSet() {
        return to(IntMutableSet::empty);
    }

    default <C extends IntMutableCollection> C takeTo(final Supplier<C> collectionFactory, final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfInt iterable = this;
        if (iterable instanceof IntMutableCollection) {
            final IntMutableCollection c = (IntMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        int count = 0;
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final int value = iterator.nextInt();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends IntMutableCollection> C skipTo(final Supplier<C> collectionFactory, final int count) {
        final C collection = collectionFactory.get();
        int counter = 0;
        for (final PrimitiveIterator.OfInt iterator = this.iterator(); iterator.hasNext(); ) {
            final int value = iterator.nextInt();
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }
}
