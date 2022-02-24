package hzt.iterables.primitives;

import hzt.collections.primitives.LongCollection;
import hzt.collections.primitives.LongListX;
import hzt.collections.primitives.LongMutableListX;
import hzt.collectors.primitves.LongCollector;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface LongCollectable extends LongIterable, PrimitiveCollectable<LongCollection> {

    default <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> accumulator) {
        PrimitiveIterator.OfLong iterator = iterator();
        final R result = supplier.get();
        while (iterator.hasNext()) {
            final long i = iterator.nextLong();
            accumulator.accept(result, i);
        }
        return result;
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
        LongMutableListX list = LongMutableListX.empty();
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            list.add(iterator.nextLong());
        }
        return list;
    }

    default LongMutableListX toMutableList() {
        LongMutableListX list = LongMutableListX.empty();
        PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            list.add(iterator.nextLong());
        }
        return list;
    }
}
