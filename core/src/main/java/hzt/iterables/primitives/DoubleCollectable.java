package hzt.iterables.primitives;

import hzt.collections.primitives.DoubleCollection;
import hzt.collections.primitives.DoubleListX;
import hzt.collections.primitives.DoubleMutableCollection;
import hzt.collections.primitives.DoubleMutableListX;
import hzt.collectors.primitves.DoubleCollector;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface DoubleCollectable extends DoubleIterable, PrimitiveCollectable<DoubleCollection> {

    default <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> accumulator) {
        PrimitiveIterator.OfDouble iterator = iterator();
        final var result = supplier.get();
        while (iterator.hasNext()) {
            final var i = iterator.nextDouble();
            accumulator.accept(result, i);
        }
        return result;
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
        return to(DoubleMutableListX::empty);
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
}
