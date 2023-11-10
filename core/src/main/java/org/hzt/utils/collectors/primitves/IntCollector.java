package org.hzt.utils.collectors.primitves;


import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface IntCollector<A, R> {

    Supplier<A> supplier();

    ObjIntConsumer<A> accumulator();

    BinaryOperator<A> combiner();

    Function<A, R> finisher();

    Set<Collector.Characteristics> characteristics();


    static <A, R> IntCollector<A, R> of(Supplier<A> supplier,
                                        ObjIntConsumer<A> accumulator,
                                        Function<A, R> finisher) {
        return new IntCollectorImpl<>(supplier, accumulator, finisher);
    }

    static <A> IntCollector<A, A> of(Supplier<A> supplier,
                                     ObjIntConsumer<A> accumulator,
                                     BinaryOperator<A> combiner,
                                     Collector.Characteristics... characteristics) {
        final Collector.Characteristics identityFinish = Collector.Characteristics.IDENTITY_FINISH;
        Set<Collector.Characteristics> cs = (characteristics.length == 0)
                ? Collections.singleton(identityFinish)
                : Collections.unmodifiableSet(EnumSet.of(identityFinish, characteristics));
        return new IntCollectorImpl<>(supplier, accumulator, combiner, cs);
    }

    static <A, R> IntCollector<A, R> of(Supplier<A> supplier,
                                        ObjIntConsumer<A> accumulator,
                                        BinaryOperator<A> combiner,
                                        Function<A, R> finisher,
                                        Collector.Characteristics... characteristics) {
        Set<Collector.Characteristics> cs = Collections.emptySet();
        if (characteristics.length > 0) {
            cs = EnumSet.noneOf(Collector.Characteristics.class);
            Collections.addAll(cs, characteristics);
            cs = Collections.unmodifiableSet(cs);
        }
        return new IntCollectorImpl<>(supplier, accumulator, combiner, finisher, cs);
    }
}
