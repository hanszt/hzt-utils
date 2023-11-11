package org.hzt.utils.gatherers;


import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;


/**
 * An intermediate operation that transforms a stream of input elements into a
 * stream of output elements, optionally applying a final action when the end of
 * the upstream is reached. The transformation may be stateless or stateful,
 * and a Gatherer may buffer arbitrarily much input before producing any output.
 *
 * <p>Gatherer operations can be performed either sequentially,
 * or be parallelized -- if a combiner function is supplied.
 *
 * <p>Examples of gathering operations include, but is not limited to:
 * grouping elements into batches (windowing functions);
 * de-duplicating consecutively similar elements; incremental accumulation
 * functions (prefix scan); incremental reordering functions, etc.  The class
 * {@link Gatherers} provides implementations of common
 * gathering operations.
 *
 * @param <T> the type of input elements to the gatherer operation
 * @param <A> the potentially mutable state type of the gatherer operation
 *            (often hidden as an implementation detail)
 * @param <R> the type of output elements from the gatherer operation
 * @apiNote <p>A {@code Gatherer} is specified by four functions that work together to
 * process input elements, optionally using intermediate state, and optionally
 * perform a final operation at the end of input.  They are: <ul>
 * <li>creating a new, potentially mutable, state ({@link #initializer()}
 * )</li>
 * <li>integrating a new input element ({@link #integrator()})</li>
 * <li>combining two states into one ({@link #combiner()})</li>
 * <li>performing an optional final operation ({@link #finisher()})</li>
 * </ul>
 *
 * <p>Each invocation to {@link #initializer()}, {@link #integrator()},
 * {@link #combiner()}, and {@link #finisher()} must return a semantically
 * identical result.
 *
 * <p>Implementations of Gatherer must not capture, retain, or expose to
 * other threads, the references to the state instance, or the downstream
 * {@link Downstream} for longer than the invocation duration of the method
 * which they are passed to.
 *
 * <p>Performing a gathering operation with a {@code Gatherer} should produce a
 * result equivalent to:
 *
 * <pre>{@code
 *     Gatherer.Downstream<? super R> downstream = ...;
 *     A state = gatherer.initializer().get();
 *     for (T t : data) {
 *         gatherer.integrator().integrate(state, t, downstream);
 *     }
 *     gatherer.finisher().accept(state, downstream);
 * }</pre>
 *
 * <p>However, the library is free to partition the input, perform the
 * integrations on the partitions, and then use the combiner function to
 * combine the partial results to achieve a gathering operation.  (Depending
 * on the specific gathering operation, this may perform better or worse,
 * depending on the relative cost of the integrator and combiner functions.)
 *
 * <p>In addition to the predefined implementations in {@link Gatherers}, the
 * static factory methods {@code of(...)} and {@code ofSequential(...)}
 * can be used to construct gatherers.  For example, you could create a gatherer
 * that implements the equivalent of
 * {@link java.util.stream.Stream#map(Function)} with:
 *
 * <pre>{@code
 * public static <T, R> Gatherer<T, ?, R> map(Function<? super T, ? extends R> mapper) {
 *     return Gatherer.of(
 *         (unused, element, downstream) -> // integrator
 *             downstream.push(mapper.apply(element))
 *     );
 * }
 * }</pre>
 *
 * <p>Gatherers are designed to be <em>composed</em>; two or more Gatherers can
 * be composed into a single Gatherer using the {@link #andThen(Gatherer)}
 * method.
 *
 * <pre>{@code
 * // using the implementation of `map` as seen above
 * Gatherer<Integer, ?, Integer> increment = map(i -> i + 1);
 *
 * Gatherer<Object, ?, String> toString = map(i -> i.toString());
 *
 * Gatherer<Integer, ?, String> incrementThenToString = plusOne.andThen(intToString);
 * }</pre>
 * <p>
 * Aa an example, in order to create a gatherer to implement a sequential
 * Prefix Scan as a Gatherer, it could be done the following way:
 *
 * <pre>{@code
 * public static <T, R> Gatherer<T, ?, R> scan(
 *     Supplier<R> initial,
 *     BiFunction<? super R, ? super T, ? extends R> scanner) {
 *
 *     class State {
 *         R current = initial.get();
 *     }
 *
 *     return Gatherer.<T, State, R>ofSequential(
 *          State::new,
 *          Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
 *              state.current = scanner.apply(state.current, element);
 *              return downstream.push(state.current);
 *          })
 *     );
 * }
 * }</pre>
 * @implSpec Libraries that implement transformation based on {@code Gatherer},
 * such as {@link org.hzt.utils.streams.StreamX#gather(Gatherer)}, must adhere to the following
 * constraints:
 * <ul>
 *     <li>Gatherers whose initializer is {@link #defaultInitializer()} are
 *     considered to be stateless, and invoking their initializer is optional.
 *     </li>
 *     <li>Gatherers whose integrator is an instance of {@link Integrator.Greedy}
 *     can be assumed not to short-circuit, and the return value of invoking
 *     {@link Integrator#integrate(Object, Object, Downstream)} does not need to
 *     be inspected.</li>
 *     <li>The first argument passed to the integration function, both
 *     arguments passed to the combiner function, and the argument passed to the
 *     finisher function must be the result of a previous invocation of the
 *     initializer, integrator, or combiner functions.</li>
 *     <li>The implementation should not do anything with the result of any of
 *     the initializer, integrator, or combiner functions other than to
 *     pass them again to the integrator, combiner, or finisher functions.</li>
 *     <li>Once a state object is passed to the combiner or finisher function,
 *     it is never passed to the integrator function again.</li>
 *     <li>When the integrator function returns {@code false},
 *     it shall be interpreted just as if there were no more elements to pass
 *     it.</li>
 *     <li>For parallel evaluation, the gathering implementation must manage
 *     that the input is properly partitioned, that partitions are processed
 *     in isolation, and combining happens only after integration is complete
 *     for both partitions.</li>
 *     <li>Gatherers whose combiner is {@link #defaultCombiner()} may only be
 *     evaluated sequentially. All other combiners allow the operation to be
 *     parallelized by initializing each partition in separation, invoking
 *     the integrator until it returns {@code false}, and then joining each
 *     partitions state using the combiner, and then invoking the finisher on
 *     the joined state. Outputs and state later in the input sequence will
 *     be discarded if processing an earlier segment short-circuits.</li>
 *     <li>Gatherers whose finisher is {@link #defaultFinisher()} are considered
 *     to not have an end-of-stream hook and invoking their finisher is
 *     optional.</li>
 * </ul>
 * @see Gatherers
 */
public interface Gatherer<T, A, R> {
    /**
     * A function that produces an instance of the intermediate state used for
     * this gathering operation.
     *
     * <p>By default, this method returns {@link #defaultInitializer()}
     *
     * @return A function that produces an instance of the intermediate state
     * used for this gathering operation
     */
    default Supplier<A> initializer() {
        return defaultInitializer();
    }

    /**
     * A function which integrates provided elements, potentially using
     * the provided intermediate state, optionally producing output to the
     * provided {@link Downstream}.
     *
     * @return a function which integrates provided elements, potentially using
     * the provided state, optionally producing output to the provided
     * Downstream
     */
    public Integrator<A, T, R> integrator();

    /**
     * A function which accepts two intermediate states and combines them into
     * one.
     *
     * <p>By default, this method returns {@link #defaultCombiner()}
     *
     * @return a function which accepts two intermediate states and combines
     * them into one
     */
    default BinaryOperator<A> combiner() {
        return defaultCombiner();
    }

    /**
     * A function which accepts the final intermediate state
     * and a {@link Downstream} object, allowing to perform a final action at
     * the end of input elements.
     *
     * <p>By default, this method returns {@link #defaultFinisher()}
     *
     * @return a function which transforms the intermediate result to the final
     * result(s) which are then passed on to the provided Downstream
     */
    default BiConsumer<A, Downstream<? super R>> finisher() {
        return defaultFinisher();
    }

    /**
     * Returns a composed Gatherer which connects the output of this Gatherer
     * to the input of that Gatherer.
     *
     * @param that the other gatherer
     * @param <AA> The type of the state of that Gatherer
     * @param <RR> The type of output of that Gatherer
     * @return returns a composed Gatherer which connects the output of this
     * Gatherer as input that Gatherer
     * @throws NullPointerException if the argument is null
     */
    default <AA, RR> Gatherer<T, ?, RR> andThen(final Gatherer<? super R, AA, ? extends RR> that) {
        Objects.requireNonNull(that);
        return Gatherers.Composite.of(this, that);
    }

    /**
     * A Custom method to end a gatherer chain with a collector
     *
     * @param collector The collector used as terminal operation
     * @param <A1>      the type of the Collector container
     * @param <V>       The result type of the Collector
     * @return A collector
     */
    default <A1, V> Collector<T, ?, V> collect(final Collector<R, A1, V> collector) {
        class Initializers<A1, A2> {
            final A1 current;
            final A2 after;

            public Initializers(A1 current, A2 after) {
                this.current = current;
                this.after = after;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                final Initializers<?, ?> that = (Initializers<?, ?>) o;
                return Objects.equals(current, that.current) && Objects.equals(after, that.after);
            }

            @Override
            public int hashCode() {
                return Objects.hash(current, after);
            }

            @Override
            public String toString() {
                return "Initializers{" +
                        "current=" + current +
                        ", after=" + after +
                        '}';
            }
        }
        return new Collector<T, Initializers<A, A1>, V>() {

            @Override
            public Supplier<Initializers<A, A1>> supplier() {
                return () -> new Initializers<>(Gatherer.this.initializer().get(), collector.supplier().get());
            }

            @Override
            public BiConsumer<Initializers<A, A1>, T> accumulator() {
                return (initializers, t) ->
                        integrator().integrate(initializers.current, t, r -> {
                            collector.accumulator().accept(initializers.after, r);
                            return true;
                        });

            }

            @Override
            public BinaryOperator<Initializers<A, A1>> combiner() {
                return (initializers, other) -> new Initializers<>(
                        Gatherer.this.combiner().apply(initializers.current, other.current),
                        collector.combiner().apply(initializers.after, other.after)
                );
            }

            @Override
            public Function<Initializers<A, A1>, V> finisher() {
                return initializers -> {
                    Gatherer.this.finisher().accept(initializers.current, r -> {
                        collector.accumulator().accept(initializers.after, r);
                        return true;
                    });
                    return collector.finisher().apply(initializers.after);
                };
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        };
    }

    /**
     * Returns an initializer which is the default initializer of a Gatherer.
     * The returned initializer identifies that the owner Gatherer is stateless.
     *
     * @param <A> the type of the state of the returned initializer
     * @return the instance of the default initializer
     * @see Gatherer#initializer()
     */
    static <A> Supplier<A> defaultInitializer() {
        return Gatherers.Value.DEFAULT.initializer();
    }

    /**
     * Returns a combiner which is the default combiner of a Gatherer.
     * The returned combiner identifies that the owning Gatherer must only
     * be evaluated sequentially.
     *
     * @param <A> the type of the state of the returned combiner
     * @return the instance of the default combiner
     * @see Gatherer#finisher()
     */
    static <A> BinaryOperator<A> defaultCombiner() {
        return Gatherers.Value.DEFAULT.combiner();
    }

    /**
     * Returns a {@code finisher} which is the default finisher of
     * a {@code Gatherer}.
     * The returned finisher identifies that the owning Gatherer performs
     * no additional actions at the end of input.
     *
     * @param <A> the type of the state of the returned finisher
     * @param <R> the type of the Downstream of the returned finisher
     * @return the instance of the default finisher
     * @see Gatherer#finisher()
     */
    static <A, R> BiConsumer<A, Downstream<? super R>> defaultFinisher() {
        return Gatherers.Value.DEFAULT.finisher();
    }

    /**
     * Returns a new, sequential, and stateless {@code Gatherer} described by
     * the given {@code integrator}.
     *
     * @param integrator the integrator function for the new gatherer
     * @param <T>        the type of input elements for the new gatherer
     * @param <R>        the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if the argument is null
     */
    static <T, R> Gatherer<T, Void, R> ofSequential(
            final Integrator<Void, T, R> integrator) {
        return of(
                defaultInitializer(),
                integrator,
                defaultCombiner(),
                defaultFinisher()
        );
    }

    /**
     * Returns a new, sequential, and stateless {@code Gatherer} described by
     * the given {@code integrator} and {@code finisher}.
     *
     * @param integrator the integrator function for the new gatherer
     * @param finisher   the finisher function for the new gatherer
     * @param <T>        the type of input elements for the new gatherer
     * @param <R>        the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if any argument is null
     */
    static <T, R> Gatherer<T, Void, R> ofSequential(
            final Integrator<Void, T, R> integrator,
            final BiConsumer<Void, Downstream<? super R>> finisher) {
        return of(
                defaultInitializer(),
                integrator,
                defaultCombiner(),
                finisher
        );
    }

    /**
     * Returns a new, sequential, {@code Gatherer} described by the given
     * {@code initializer} and {@code integrator}.
     *
     * @param initializer the supplier function for the new gatherer
     * @param integrator  the integrator function for the new gatherer
     * @param <T>         the type of input elements for the new gatherer
     * @param <A>         the type of initializer for the new gatherer
     * @param <R>         the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if any argument is null
     */
    static <T, A, R> Gatherer<T, A, R> ofSequential(
            final Supplier<A> initializer,
            final Integrator<A, T, R> integrator) {
        return of(
                initializer,
                integrator,
                defaultCombiner(),
                defaultFinisher()
        );
    }

    /**
     * Returns a new, sequential, {@code Gatherer} described by the given
     * {@code initializer}, {@code integrator}, and {@code finisher}.
     *
     * @param initializer the supplier function for the new gatherer
     * @param integrator  the integrator function for the new gatherer
     * @param finisher    the finisher function for the new gatherer
     * @param <T>         the type of input elements for the new gatherer
     * @param <A>         the type of initializer for the new gatherer
     * @param <R>         the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if any argument is null
     */
    static <T, A, R> Gatherer<T, A, R> ofSequential(
            final Supplier<A> initializer,
            final Integrator<A, T, R> integrator,
            final BiConsumer<A, Downstream<? super R>> finisher) {
        return of(
                initializer,
                integrator,
                defaultCombiner(),
                finisher
        );
    }

    /**
     * Returns a new, parallelizable, and stateless {@code Gatherer} described
     * by the given {@code integrator}.
     *
     * @param integrator the integrator function for the new gatherer
     * @param <T>        the type of input elements for the new gatherer
     * @param <R>        the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if any argument is null
     */
    static <T, R> Gatherer<T, Void, R> of(final Integrator<Void, T, R> integrator) {
        return of(
                defaultInitializer(),
                integrator,
                Gatherers.Value.DEFAULT.statelessCombiner,
                defaultFinisher()
        );
    }

    /**
     * Returns a new, parallelizable, and stateless {@code Gatherer} described
     * by the given {@code integrator} and {@code finisher}.
     *
     * @param integrator the integrator function for the new gatherer
     * @param finisher   the finisher function for the new gatherer
     * @param <T>        the type of input elements for the new gatherer
     * @param <R>        the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if any argument is null
     */
    static <T, R> Gatherer<T, Void, R> of(
            final Integrator<Void, T, R> integrator,
            final BiConsumer<Void, Downstream<? super R>> finisher) {
        return of(
                defaultInitializer(),
                integrator,
                Gatherers.Value.DEFAULT.statelessCombiner,
                finisher
        );
    }

    /**
     * Returns a new, parallelizable, {@code Gatherer} described by the given
     * {@code initializer}, {@code integrator}, {@code combiner} and
     * {@code finisher}.
     *
     * @param initializer the supplier function for the new gatherer
     * @param integrator  the integrator function for the new gatherer
     * @param combiner    the combiner function for the new gatherer
     * @param finisher    the finisher function for the new gatherer
     * @param <T>         the type of input elements for the new gatherer
     * @param <A>         the type of initializer for the new gatherer
     * @param <R>         the type of results for the new gatherer
     * @return the new {@code Gatherer}
     * @throws NullPointerException if any argument is null
     */
    static <T, A, R> Gatherer<T, A, R> of(
            final Supplier<A> initializer,
            final Integrator<A, T, R> integrator,
            final BinaryOperator<A> combiner,
            final BiConsumer<A, Downstream<? super R>> finisher) {
        return new Gatherers.GathererImpl<>(
                Objects.requireNonNull(initializer),
                Objects.requireNonNull(integrator),
                Objects.requireNonNull(combiner),
                Objects.requireNonNull(finisher)
        );
    }

    /**
     * A Downstream object is the next stage in a pipeline of operations,
     * to which elements can be sent.
     *
     * @param <T> the type of elements this downstream accepts
     */
    @FunctionalInterface
    interface Downstream<T> {

        /**
         * Pushes, if possible, the provided element to the next stage in the
         * pipeline.
         *
         * <p>If this method returns {@code false} then the next stage does
         * not accept any more elements.
         *
         * @param element the element to send
         * @return {@code true} if more elements can be sent,
         * and {@code false} if not.
         */
        boolean push(T element);

        /**
         * Allows for checking whether the next stage is known to not want
         * any more elements sent to it.
         *
         * @return {@code true} if this Downstream is known not to want any
         * more elements sent to it, {@code false} if otherwise
         * @apiNote This is best-effort only, once this returns true it should
         * never return false again for the same instance.
         * <p>
         * By default, this method returns {@code false}.
         */
        default boolean isRejecting() {
            return false;
        }
    }
}