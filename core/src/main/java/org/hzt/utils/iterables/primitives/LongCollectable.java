package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.LongCollection;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableCollection;
import org.hzt.utils.collections.primitives.LongMutableList;
import org.hzt.utils.collections.primitives.LongMutableSet;
import org.hzt.utils.collectors.primitves.LongCollector;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface LongCollectable extends PrimitiveCollectable<LongCollection>, PrimitiveIterable.OfLong {

    default <R> R collect(final Supplier<R> supplier,
                          final ObjLongConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(final Supplier<A> supplier,
                             final ObjLongConsumer<A> accumulator,
                             final Function<? super A, ? extends R> finisher) {
        final PrimitiveIterator.OfLong iterator = iterator();
        final A result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextLong());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(final LongCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            final LongCollector<A1, R1> downStream1,
            final LongCollector<A2, R2> downStream2,
            final BiFunction<? super R1, ? super R2, ? extends R> combiner) {
        final A1 result1 = downStream1.supplier().get();
        final A2 result2 = downStream2.supplier().get();
        final ObjLongConsumer<A1> accumulator1 = downStream1.accumulator();
        final ObjLongConsumer<A2> accumulator2 = downStream2.accumulator();
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final long value = iterator.nextLong();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default LongList toList() {
        return LongList.copyOf(toMutableList());
    }

    default <C extends LongMutableCollection> C to(final Supplier<C> collectionFactory) {
        final C collection = collectionFactory.get();
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            collection.add(iterator.nextLong());
        }
        return collection;
    }

    default LongMutableList toMutableList() {
       return to(LongMutableList::empty);
    }

    @Override
    default LongMutableSet toMutableSet() {
        return to(LongMutableSet::empty);
    }

    default <C extends LongMutableCollection> C takeTo(final Supplier<C> collectionFactory, final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfLong iterable = this;
        if (iterable instanceof LongMutableCollection) {
            final LongMutableCollection c = (LongMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        int count = 0;
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final long value = iterator.nextLong();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends LongMutableCollection> C skipTo(final Supplier<C> collectionFactory, final int count) {
        final C collection = collectionFactory.get();
        int counter = 0;
        for (final PrimitiveIterator.OfLong iterator = this.iterator(); iterator.hasNext(); ) {
            final long value = iterator.nextLong();
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }
}
