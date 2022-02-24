package hzt.collectors.primitves;

import org.jetbrains.annotations.NotNull;

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

    static <A, R> LongCollector<A, R> of(@NotNull Supplier<A> supplier,
                                         @NotNull ObjLongConsumer<A> accumulator,
                                         @NotNull Function<A, R> finisher) {
        return new LongCollectorImpl<>(supplier, accumulator, finisher);
    }

    static <A, R> LongCollector<A, R> of(@NotNull Supplier<A> supplier,
                                         @NotNull ObjLongConsumer<A> accumulator,
                                         @NotNull BinaryOperator<A> combiner,
                                         @NotNull Collector.Characteristics... characteristics) {
        final Collector.Characteristics identityFinish = Collector.Characteristics.IDENTITY_FINISH;
        Set<Collector.Characteristics> cs = (characteristics.length == 0)
                ? Collections.singleton(identityFinish)
                : Collections.unmodifiableSet(EnumSet.of(identityFinish, characteristics));
        return new LongCollectorImpl<>(supplier, accumulator, combiner, cs);
    }

    static <A, R> LongCollector<A, R> of(@NotNull Supplier<A> supplier,
                                         @NotNull ObjLongConsumer<A> accumulator,
                                         @NotNull BinaryOperator<A> combiner,
                                         @NotNull Function<A, R> finisher,
                                         @NotNull Collector.Characteristics... characteristics) {
        Set<Collector.Characteristics> cs = Collections.emptySet();
        if (characteristics.length > 0) {
            cs = EnumSet.noneOf(Collector.Characteristics.class);
            Collections.addAll(cs, characteristics);
            cs = Collections.unmodifiableSet(cs);
        }
        return new LongCollectorImpl<>(supplier, accumulator, combiner, finisher, cs);
    }
}
