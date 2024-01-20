package org.hzt.utils.gatherers.primitives;

import org.hzt.utils.gatherers.Gatherer;
import org.hzt.utils.gatherers.Integrator;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

@FunctionalInterface
public interface IntGatherer<A, R> extends Gatherer<Integer, A, R> {

    static <A, R> IntGatherer<A, R> ofSequential(final Supplier<A> initializer, final IntIntegrator<A, R> integrator) {
        return ofSequential(initializer, integrator, (s, downstream) -> {
            // No finisher
        });
    }

    static <A, R> IntGatherer<A, R> ofSequential(final Supplier<A> initializer, final IntIntegrator<A, R> integrator, final BiConsumer<A, Gatherer.Downstream<? super R>> finisher) {
        return new IntGatherer<>() {

            @Override
            public Supplier<A> initializer() {
                return initializer;
            }

            @Override
            public IntIntegrator<A, R> intIntegrator() {
                return integrator;
            }

            @Override
            public BiConsumer<A, Gatherer.Downstream<? super R>> finisher() {
                return finisher;
            }
        };
    }

    static <A, R> IntGatherer<A, R> of(final IntIntegrator<A, R> integrator) {
        return new IntGatherer<>() {
            @Override
            public IntIntegrator<A, R> intIntegrator() {
                return integrator;
            }

            @Override
            public BinaryOperator<A> combiner() {
                // A stateless combiner
                //noinspection ReturnOfNull
                return (a1, a2) -> null;
            }
        };
    }

    IntIntegrator<A, R> intIntegrator();

    default Integrator<A, Integer, R> integrator() {
        return (state, integer, result) -> intIntegrator().integrate(state, integer, result);
    }
}
