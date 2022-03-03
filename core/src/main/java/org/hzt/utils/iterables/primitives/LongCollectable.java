package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.LongCollection;
import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.collections.primitives.LongMutableCollection;
import org.hzt.utils.collections.primitives.LongMutableListX;
import org.hzt.utils.collectors.primitves.LongCollector;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface LongCollectable extends LongIterable, PrimitiveCollectable<LongCollection> {

    default <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator) {
        PrimitiveIterator.OfLong iterator = iterator();
        final var result = supplier.get();
        while (iterator.hasNext()) {
            final var i = iterator.nextLong();
            accumulator.accept(result, i);
        }
        return result;
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull LongCollector<A1, R1> downStream1,
            @NotNull LongCollector<A2, R2> downStream2,
            @NotNull BiFunction<R1, R2, R> combiner) {
        final var result1 = downStream1.supplier().get();
        final var result2 = downStream2.supplier().get();
        final var accumulator1 = downStream1.accumulator();
        final var accumulator2 = downStream2.accumulator();
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextLong();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default LongListX toListX() {
        return to(LongMutableListX::empty);
    }

    default <C extends LongMutableCollection> C to(Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            collection.add(iterator.nextLong());
        }
        return collection;
    }

    default LongMutableListX toMutableList() {
       return to(LongMutableListX::empty);
    }
}
