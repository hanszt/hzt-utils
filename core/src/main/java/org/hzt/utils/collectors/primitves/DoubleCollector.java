package org.hzt.utils.collectors.primitves;


import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface DoubleCollector<A, R> {

    Supplier<A> supplier();

    ObjDoubleConsumer<A> accumulator();
    
    BinaryOperator<A> combiner();

    Function<A, R> finisher();
    
    Set<Collector.Characteristics> characteristics();

    static <A, R> DoubleCollector<A, R> of(final Supplier<A> supplier,
                                           final ObjDoubleConsumer<A> accumulator,
                                           final Function<A, R> finisher) {
        return new DoubleCollectorImpl<>(supplier, accumulator, finisher);
    }

    static <A, R> DoubleCollector<A, R> of(final Supplier<A> supplier,
                                           final ObjDoubleConsumer<A> accumulator,
                                           final BinaryOperator<A> combiner,
                                           final Collector.Characteristics... characteristics) {
        final Collector.Characteristics identityFinish = Collector.Characteristics.IDENTITY_FINISH;
        final Set<Collector.Characteristics> cs = (characteristics.length == 0)
                ? Collections.singleton(identityFinish)
                : Collections.unmodifiableSet(EnumSet.of(identityFinish, characteristics));
        return new DoubleCollectorImpl<>(supplier, accumulator, combiner, cs);
    }

    static <A, R> DoubleCollector<A, R> of(final Supplier<A> supplier,
                                           final ObjDoubleConsumer<A> accumulator,
                                           final BinaryOperator<A> combiner,
                                           final Function<A, R> finisher,
                                           final Collector.Characteristics... characteristics) {
        Set<Collector.Characteristics> cs = Collections.emptySet();
        if (characteristics.length > 0) {
            cs = EnumSet.noneOf(Collector.Characteristics.class);
            Collections.addAll(cs, characteristics);
            cs = Collections.unmodifiableSet(cs);
        }
        return new DoubleCollectorImpl<>(supplier, accumulator, combiner, finisher, cs);
    }
}
