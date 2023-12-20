package org.hzt.utils.collectors.primitves;


import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface LongCollector<A, R> {

    Supplier<A> supplier();

    ObjLongConsumer<A> accumulator();

    BinaryOperator<A> combiner();

    Function<A, R> finisher();

    Set<Collector.Characteristics> characteristics();

    static <A, R> LongCollector<A, R> of(final Supplier<A> supplier,
                                         final ObjLongConsumer<A> accumulator,
                                         final Function<A, R> finisher) {
        return new LongCollectorImpl<>(supplier, accumulator, finisher);
    }

    static <A, R> LongCollector<A, R> of(final Supplier<A> supplier,
                                         final ObjLongConsumer<A> accumulator,
                                         final BinaryOperator<A> combiner,
                                         final Collector.Characteristics... characteristics) {
        final var identityFinish = Collector.Characteristics.IDENTITY_FINISH;
        final var cs = (characteristics.length == 0)
                ? Set.of(identityFinish)
                : Collections.unmodifiableSet(EnumSet.of(identityFinish, characteristics));
        return new LongCollectorImpl<>(supplier, accumulator, combiner, cs);
    }

    static <A, R> LongCollector<A, R> of(final Supplier<A> supplier,
                                         final ObjLongConsumer<A> accumulator,
                                         final BinaryOperator<A> combiner,
                                         final Function<A, R> finisher,
                                         final Collector.Characteristics... characteristics) {
        Set<Collector.Characteristics> cs = Collections.emptySet();
        if (characteristics.length > 0) {
            cs = EnumSet.noneOf(Collector.Characteristics.class);
            Collections.addAll(cs, characteristics);
            cs = Collections.unmodifiableSet(cs);
        }
        return new LongCollectorImpl<>(supplier, accumulator, combiner, finisher, cs);
    }
}
