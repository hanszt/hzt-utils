package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.LongCollection;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableCollection;
import org.hzt.utils.collections.primitives.LongMutableList;
import org.hzt.utils.collections.primitives.LongMutableSet;
import org.hzt.utils.collectors.primitves.LongCollector;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface LongCollectable extends PrimitiveCollectable<LongCollection>, PrimitiveIterable.OfLong {

    default <R> R collect(@NotNull Supplier<R> supplier,
                          @NotNull ObjLongConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(@NotNull Supplier<A> supplier,
                             @NotNull ObjLongConsumer<A> accumulator,
                             @NotNull Function<? super A, ? extends R> finisher) {
        var iterator = iterator();
        final var result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextLong());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(@NotNull LongCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull LongCollector<A1, R1> downStream1,
            @NotNull LongCollector<A2, R2> downStream2,
            @NotNull BiFunction<? super R1, ? super R2, ? extends R> combiner) {
        final var result1 = downStream1.supplier().get();
        final var result2 = downStream2.supplier().get();
        final var accumulator1 = downStream1.accumulator();
        final var accumulator2 = downStream2.accumulator();
        var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextLong();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default LongList toList() {
        return LongList.copyOf(toMutableList());
    }

    default <C extends LongMutableCollection> C to(@NotNull Supplier<C> collectionFactory) {
        var collection = collectionFactory.get();
        final var iterator = iterator();
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

    default <C extends LongMutableCollection> C takeTo(@NotNull Supplier<C> collectionFactory, long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        var collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfLong iterable = this;
        if (iterable instanceof LongMutableCollection) {
            var c = (LongMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        var count = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            var value = iterator.nextLong();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends LongMutableCollection> C skipTo(Supplier<C> collectionFactory, int count) {
        var collection = collectionFactory.get();
        var counter = 0;
        for (var iterator = this.iterator(); iterator.hasNext(); ) {
            long value = iterator.nextLong();
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }
}
