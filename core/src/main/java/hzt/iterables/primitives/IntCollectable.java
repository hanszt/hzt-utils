package hzt.iterables.primitives;

import hzt.collections.primitives.IntCollection;
import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.IntMutableCollection;
import hzt.collections.primitives.IntMutableListX;
import hzt.collectors.primitves.IntCollector;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface IntCollectable extends IntIterable, PrimitiveCollectable<IntCollection> {

    default <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator) {
        return collect(supplier, accumulator, It::self);
    }

    default <A, R> R collect(Supplier<A> supplier, ObjIntConsumer<A> accumulator, Function<A, R> finisher) {
        PrimitiveIterator.OfInt iterator = iterator();
        final var result = supplier.get();
        while (iterator.hasNext()) {
            accumulator.accept(result, iterator.nextInt());
        }
        return finisher.apply(result);
    }

    default <A1, R1, A2, R2, R> R teeing(
            @NotNull IntCollector<A1, R1> downStream1,
            @NotNull IntCollector<A2, R2> downStream2,
            @NotNull BiFunction<R1, R2, R> combiner) {
        final var result1 = downStream1.supplier().get();
        final var result2 = downStream2.supplier().get();
        final var accumulator1 = downStream1.accumulator();
        final var accumulator2 = downStream2.accumulator();
        PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextInt();
            accumulator1.accept(result1, value);
            accumulator2.accept(result2, value);
        }
        return combiner.apply(downStream1.finisher().apply(result1), downStream2.finisher().apply(result2));
    }

    default IntListX toListX() {
        return to(IntMutableListX::empty);
    }

    default <C extends IntMutableCollection> C to(Supplier<C> collectionFactory) {
        C collection = collectionFactory.get();
        final var iterator = iterator();
        while(iterator.hasNext()) {
            collection.add(iterator.nextInt());
        }
        return collection;
    }

    default IntMutableListX toMutableList() {
        return to(IntMutableListX::empty);
    }
}
