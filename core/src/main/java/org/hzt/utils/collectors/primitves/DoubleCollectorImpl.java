package org.hzt.utils.collectors.primitves;

import java.util.Collections;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

@SuppressWarnings("squid:S2384")
public final class DoubleCollectorImpl<A, R> implements DoubleCollector<A, R> {

    private final Supplier<A> supplier;
    private final ObjDoubleConsumer<A> accumulator;
    private final BinaryOperator<A> combiner;
    private final Function<A, R> finisher;
    private final Set<Collector.Characteristics> characteristics;

    DoubleCollectorImpl(Supplier<A> supplier,
                        ObjDoubleConsumer<A> accumulator,
                        BinaryOperator<A> combiner,
                        Function<A, R> finisher,
                        Set<Collector.Characteristics> characteristics) {
        this.supplier = supplier;
        this.accumulator = accumulator;
        this.combiner = combiner;
        this.finisher = finisher;
        this.characteristics = characteristics;
    }

    DoubleCollectorImpl(Supplier<A> supplier,
                        ObjDoubleConsumer<A> accumulator,
                        Function<A, R> finisher) {
        this(supplier, accumulator, (a, b) -> {
            throw new UnsupportedOperationException("This collector implementation does not support combining operations");
        }, finisher, Collections.emptySet());
    }

    DoubleCollectorImpl(Supplier<A> supplier,
                        ObjDoubleConsumer<A> accumulator,
                        BinaryOperator<A> combiner,
                        Set<Collector.Characteristics> characteristics) {
        this(supplier, accumulator, combiner, castingIdentity(), characteristics);
    }


    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    @Override
    public ObjDoubleConsumer<A> accumulator() {
        return accumulator;
    }

    @Override
    public Supplier<A> supplier() {
        return supplier;
    }

    @Override
    public BinaryOperator<A> combiner() {
        return combiner;
    }

    @Override
    public Function<A, R> finisher() {
        return finisher;
    }

    @Override
    public Set<Collector.Characteristics> characteristics() {
        return characteristics;
    }
}
