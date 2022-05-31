package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.LongCollection;
import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.collections.primitives.LongMutableCollection;
import org.hzt.utils.collections.primitives.LongMutableListX;
import org.hzt.utils.collectors.primitves.LongCollector;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface LongCollectable extends LongIterable, PrimitiveCollectable<LongCollection> {

    default <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(Supplier<A> supplier, ObjLongConsumer<A> accumulator, Function<A, R> finisher) {
        PrimitiveIterator.OfLong iterator = iterator();
        final A result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextLong());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(LongCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull LongCollector<A1, R1> downStream1,
            @NotNull LongCollector<A2, R2> downStream2,
            @NotNull BiFunction<R1, R2, R> combiner) {
        final A1 result1 = downStream1.supplier().get();
        final A2 result2 = downStream2.supplier().get();
        final ObjLongConsumer<A1> accumulator1 = downStream1.accumulator();
        final ObjLongConsumer<A2> accumulator2 = downStream2.accumulator();
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final long value = iterator.nextLong();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default LongListX toListX() {
        return toMutableList();
    }

    default <C extends LongMutableCollection> C to(Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            collection.add(iterator.nextLong());
        }
        return collection;
    }

    default LongMutableListX toMutableList() {
       return to(LongMutableListX::empty);
    }

    default <C extends LongMutableCollection> C takeTo(Supplier<C> collectionFactory, long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        C collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final LongIterable iterable = this;
        if (iterable instanceof LongMutableCollection) {
            LongMutableCollection c = (LongMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        int count = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            long value = iterator.nextLong();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }
}
