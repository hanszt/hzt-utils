package hzt.collectors;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Simple implementation class for {@code Collector}.
 *
 */
final class CollectorImpl<T, A, R> implements Collector<T, A, R> {
    private final Supplier<A> supplier;
    private final BiConsumer<A, T> accumulator;
    private final BinaryOperator<A> combiner;
    private final Function<A, R> finisher;
    private final Set<Characteristics> characteristics;

    CollectorImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner, Function<A, R> finisher, Set<Characteristics> characteristics) {
        characteristics = Collections.unmodifiableSet(characteristics);
        this.supplier = supplier;
        this.accumulator = accumulator;
        this.combiner = combiner;
        this.finisher = finisher;
        this.characteristics = characteristics;
    }

    public Supplier<A> supplier() {
        return supplier;
    }

    public BiConsumer<A, T> accumulator() {
        return accumulator;
    }

    public BinaryOperator<A> combiner() {
        return combiner;
    }

    public Function<A, R> finisher() {
        return finisher;
    }

    public Set<Characteristics> characteristics() {
        return characteristics;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        CollectorImpl<T, A,R> that = (CollectorImpl<T, A, R>) obj;
        return Objects.equals(this.supplier, that.supplier) &&
                Objects.equals(this.accumulator, that.accumulator) &&
                Objects.equals(this.combiner, that.combiner) &&
                Objects.equals(this.finisher, that.finisher) &&
                Objects.equals(this.characteristics, that.characteristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplier, accumulator, combiner, finisher, characteristics);
    }

    @Override
    public String toString() {
        return "CollectorImpl[" +
                "supplier=" + supplier + ", " +
                "accumulator=" + accumulator + ", " +
                "combiner=" + combiner + ", " +
                "finisher=" + finisher + ", " +
                "characteristics=" + characteristics + ']';
    }

}
