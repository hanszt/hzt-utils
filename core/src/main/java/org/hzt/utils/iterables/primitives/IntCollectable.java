package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.primitives.IntCollection;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableCollection;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.collections.primitives.IntMutableSet;
import org.hzt.utils.collectors.primitves.IntCollector;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface IntCollectable extends PrimitiveCollectable<IntCollection>, PrimitiveIterable.OfInt {

    default <R> R collect(@NotNull final Supplier<R> supplier,
                          @NotNull final ObjIntConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(@NotNull final Supplier<A> supplier,
                             @NotNull final ObjIntConsumer<A> accumulator,
                             @NotNull final Function<? super A, ? extends R> finisher) {
        final var iterator = iterator();
        final var result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextInt());
        }
        return finisher.apply(result);
    }

    default <A, R> R collect(@NotNull final IntCollector<A, R> collector) {
        return collect(collector.supplier(), collector.accumulator(), collector.finisher());
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull final IntCollector<A1, ? extends R1> downStream1,
            @NotNull final IntCollector<A2, ? extends R2> downStream2,
            @NotNull final BiFunction<? super R1, ? super R2, ? extends R> combiner) {
        final var result1 = downStream1.supplier().get();
        final var result2 = downStream2.supplier().get();
        final var accumulator1 = downStream1.accumulator();
        final var accumulator2 = downStream2.accumulator();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextInt();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default IntList toList() {
        return IntList.copyOf(toMutableList());
    }

    default <C extends IntMutableCollection> C to(@NotNull final Supplier<C> collectionFactory) {
        final var collection = collectionFactory.get();
        final var iterator = iterator();
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

    default <C extends IntMutableCollection> C takeTo(@NotNull final Supplier<C> collectionFactory, final long n) {
        PreConditions.requireGreaterThanOrEqualToZero(n);
        final var collection = collectionFactory.get();
        if (n == 0) {
            return collection;
        }
        final PrimitiveIterable.OfInt iterable = this;
        if (iterable instanceof IntMutableCollection) {
            final var c = (IntMutableCollection) iterable;
            if (n >= c.size()) {
                collection.addAll(c);
                return collection;
            }
        }
        var count = 0;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextInt();
            collection.add(value);
            if (++count == n) {
                break;
            }
        }
        return collection;
    }

    default <C extends IntMutableCollection> C skipTo(final Supplier<C> collectionFactory, final int count) {
        final var collection = collectionFactory.get();
        var counter = 0;
        for (final var iterator = this.iterator(); iterator.hasNext(); ) {
            final var value = iterator.nextInt();
            if (counter >= count) {
                collection.add(value);
            }
            counter++;
        }
        return collection;
    }
}
