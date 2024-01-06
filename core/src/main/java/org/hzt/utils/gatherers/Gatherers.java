/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.hzt.utils.gatherers;

import org.hzt.utils.gatherers.Gatherer.Downstream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Implementations of {@link Gatherer} that implement various useful intermediate
 * operations, such as windowing functions, folding functions,
 * transforming elements concurrently, etc.
 */
public final class Gatherers {
    private Gatherers() {
    }

    /*
     * This enum is used to provide the default functions for the factory methods
     * and for the default methods for when implementing the Gatherer interface.
     *
     * This serves the following purposes:
     * 1. removes the need for using `null` for signalling absence of specified
     *    value and thereby hiding user bugs
     * 2. allows to check against these default values to avoid calling methods
     *    needlessly
     * 3. allows for more efficient composition and evaluation
     */
    @SuppressWarnings("rawtypes")
    enum Value implements Supplier, BinaryOperator, BiConsumer {
        DEFAULT;

        final BinaryOperator<Void> statelessCombiner = (left, right) -> null;

        // BiConsumer
        @Override
        public void accept(final Object state, final Object downstream) {
            // do nothing by default
        }

        // BinaryOperator
        @Override
        public Object apply(final Object left, final Object right) {
            throw new UnsupportedOperationException("This combiner cannot be used!");
        }

        // Supplier
        @Override
        public Object get() {
            return null;
        }

        @SuppressWarnings("unchecked")
        <A> Supplier<A> initializer() {
            return this;
        }

        @SuppressWarnings("unchecked")
        <T> BinaryOperator<T> combiner() {
            return this;
        }

        @SuppressWarnings("unchecked")
        <T, R> BiConsumer<T, Downstream<? super R>> finisher() {
            return this;
        }
    }

    static final class GathererImpl<T, A, R> implements Gatherer<T, A, R> {
        private final Supplier<A> initializer;
        private final Integrator<A, T, R> integrator;
        private final BinaryOperator<A> combiner;
        private final BiConsumer<A, Downstream<? super R>> finisher;

        GathererImpl(
                final Supplier<A> initializer,
                final Integrator<A, T, R> integrator,
                final BinaryOperator<A> combiner,
                final BiConsumer<A, Downstream<? super R>> finisher) {
            this.initializer = initializer;
            this.integrator = integrator;
            this.combiner = combiner;
            this.finisher = finisher;
        }

        static <T, A, R> GathererImpl<T, A, R> of(
                final Supplier<A> initializer,
                final Integrator<A, T, R> integrator,
                final BinaryOperator<A> combiner,
                final BiConsumer<A, Downstream<? super R>> finisher) {
            return new GathererImpl<>(
                    Objects.requireNonNull(initializer, "initializer"),
                    Objects.requireNonNull(integrator, "integrator"),
                    Objects.requireNonNull(combiner, "combiner"),
                    Objects.requireNonNull(finisher, "finisher")
            );
        }

        @Override
        public Supplier<A> initializer() {
            return initializer;
        }

        @Override
        public Integrator<A, T, R> integrator() {
            return integrator;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public BiConsumer<A, Downstream<? super R>> finisher() {
            return finisher;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            final GathererImpl<?, ?, ?> that = (GathererImpl<?, ?, ?>) obj;
            return Objects.equals(this.initializer, that.initializer) &&
                    Objects.equals(this.integrator, that.integrator) &&
                    Objects.equals(this.combiner, that.combiner) &&
                    Objects.equals(this.finisher, that.finisher);
        }

        @Override
        public int hashCode() {
            return Objects.hash(initializer, integrator, combiner, finisher);
        }

        @Override
        public String toString() {
            return "GathererImpl[" +
                    "initializer=" + initializer + ", " +
                    "integrator=" + integrator + ", " +
                    "combiner=" + combiner + ", " +
                    "finisher=" + finisher + ']';
        }

    }

    static final class Composite<T, A, R, AA, RR> implements Gatherer<T, Object, RR> {
        private final Gatherer<T, A, ? extends R> left;
        private final Gatherer<? super R, AA, ? extends RR> right;
        // FIXME change `impl` to a computed constant when available
        private GathererImpl<T, Object, RR> impl;

        static <T, A, R, AA, RR> Composite<T, A, R, AA, RR> of(
                final Gatherer<T, A, ? extends R> left,
                final Gatherer<? super R, AA, ? extends RR> right) {
            return new Composite<>(left, right);
        }

        private Composite(final Gatherer<T, A, ? extends R> left,
                          final Gatherer<? super R, AA, ? extends RR> right) {
            this.left = left;
            this.right = right;
        }

        @SuppressWarnings("unchecked")
        private GathererImpl<T, Object, RR> impl() {
            // ATTENTION: this method currently relies on a "benign" data-race
            // as it should deterministically produce the same result even if
            // initialized concurrently on different threads.
            final GathererImpl<T, Object, RR> i = impl;
            return i != null ? i : (impl = (GathererImpl<T, Object, RR>) impl(left, right));
        }

        @Override
        public Supplier<Object> initializer() {
            return impl().initializer();
        }

        @Override
        public Integrator<Object, T, RR> integrator() {
            return impl().integrator();
        }

        @Override
        public BinaryOperator<Object> combiner() {
            return impl().combiner();
        }

        @Override
        public BiConsumer<Object, Downstream<? super RR>> finisher() {
            return impl().finisher();
        }

        @Override
        public <AAA, RRR> Gatherer<T, ?, RRR> andThen(
                final Gatherer<? super RR, AAA, ? extends RRR> that) {
            if (that.getClass() == Composite.class) {
                @SuppressWarnings("unchecked") final Composite<? super RR, ?, Object, ?, ? extends RRR> c =
                        (Composite<? super RR, ?, Object, ?, ? extends RRR>) that;
                return left.andThen(right.andThen(c.left).andThen(c.right));
            } else {
                return left.andThen(right.andThen(that));
            }
        }

        static <T, A, R, AA, RR> GathererImpl<T, ?, RR> impl(
                final Gatherer<T, A, R> left, final Gatherer<? super R, AA, RR> right) {
            final Supplier<A> leftInitializer = left.initializer();
            final Integrator<A, T, R> leftIntegrator = left.integrator();
            final BinaryOperator<A> leftCombiner = left.combiner();
            final BiConsumer<A, Downstream<? super R>> leftFinisher = left.finisher();

            final Supplier<AA> rightInitializer = right.initializer();
            final Integrator<AA, ? super R, RR> rightIntegrator = right.integrator();
            final BinaryOperator<AA> rightCombiner = right.combiner();
            final BiConsumer<AA, Downstream<? super RR>> rightFinisher = right.finisher();

            final boolean leftStateless = leftInitializer == Gatherer.defaultInitializer();
            final boolean rightStateless = rightInitializer == Gatherer.defaultInitializer();

            final boolean leftGreedy = leftIntegrator instanceof Integrator.Greedy;
            final boolean rightGreedy = rightIntegrator instanceof Integrator.Greedy;

            /*
             * For pairs of stateless and greedy Gatherers, we can optimize
             * evaluation as we do not need to track any state nor any
             * short-circuit signals. This can provide significant
             * performance improvements.
             */
            if (leftStateless && rightStateless && leftGreedy && rightGreedy) {
                return new GathererImpl<>(
                        Gatherer.defaultInitializer(),
                        Integrator.ofGreedy((unused, element, downstream) ->
                                leftIntegrator.integrate(
                                        null,
                                        element,
                                        r -> rightIntegrator.integrate(null, r, downstream))
                        ),
                        (leftCombiner == Gatherer.defaultCombiner()
                                || rightCombiner == Gatherer.defaultCombiner())
                                ? Gatherer.defaultCombiner()
                                : Value.DEFAULT.statelessCombiner
                        ,
                        (leftFinisher == Gatherer.<A, R>defaultFinisher()
                                && rightFinisher == Gatherer.<AA, RR>defaultFinisher())
                                ? Gatherer.defaultFinisher()
                                : (unused, downstream) -> {
                            if (leftFinisher != Gatherer.<A, R>defaultFinisher())
                                leftFinisher.accept(
                                        null,
                                        r -> rightIntegrator.integrate(null, r, downstream));
                            if (rightFinisher != Gatherer.<AA, RR>defaultFinisher())
                                rightFinisher.accept(null, downstream);
                        }
                );
            } else {
                class State {
                    final A leftState;
                    final AA rightState;
                    boolean leftProceed;
                    boolean rightProceed;

                    private State(final A leftState, final AA rightState,
                                  final boolean leftProceed, final boolean rightProceed) {
                        this.leftState = leftState;
                        this.rightState = rightState;
                        this.leftProceed = leftProceed;
                        this.rightProceed = rightProceed;
                    }

                    State() {
                        this(leftStateless ? null : leftInitializer.get(),
                                rightStateless ? null : rightInitializer.get(),
                                true, true);
                    }

                    State joinLeft(final State right) {
                        return new State(
                                leftStateless ? null : leftCombiner.apply(this.leftState, right.leftState),
                                rightStateless ? null : rightCombiner.apply(this.rightState, right.rightState),
                                this.leftProceed && this.rightProceed,
                                right.leftProceed && right.rightProceed);
                    }

                    boolean integrate(final T t, final Downstream<? super RR> c) {
                        /*
                         * rightProceed must be checked after integration of
                         * left since that can cause right to short-circuit
                         * We always want to conditionally write leftProceed
                         * here, which means that we only do so if we are
                         * known to be not-greedy.
                         */
                        return (leftIntegrator.integrate(leftState, t, r -> rightIntegrate(r, c))
                                || leftGreedy
                                || (leftProceed = false))
                                && (rightGreedy || rightProceed);
                    }

                    void finish(final Downstream<? super RR> c) {
                        if (leftFinisher != Gatherer.<A, R>defaultFinisher())
                            leftFinisher.accept(leftState, r -> rightIntegrate(r, c));
                        if (rightFinisher != Gatherer.<AA, RR>defaultFinisher())
                            rightFinisher.accept(rightState, c);
                    }

                    /*
                     * Currently we use the following to ferry elements from
                     * the left Gatherer to the right Gatherer, but we create
                     * the Gatherer.Downstream as a lambda which means that
                     * the default implementation of `isKnownDone()` is used.
                     *
                     * If it is determined that we want to be able to support
                     * the full interface of Gatherer.Downstream then we have
                     *  the following options:
                     *    1. Have State implement Downstream<? super R>
                     *       and store the passed in Downstream<? super RR>
                     *       downstream as an instance field in integrate()
                     *       and read it in push(R r).
                     *    2. Allocate a new Gatherer.Downstream<? super R> for
                     *       each invocation of integrate() which might prove
                     *       costly.
                     */
                    public boolean rightIntegrate(final R r, final Downstream<? super RR> downstream) {
                        // The following logic is highly performance sensitive
                        return (rightGreedy || rightProceed)
                                && (rightIntegrator.integrate(rightState, r, downstream)
                                || rightGreedy
                                || (rightProceed = false));
                    }
                }

                return new GathererImpl<T, State, RR>(
                        State::new,
                        (leftGreedy && rightGreedy)
                                ? Integrator.<State, T, RR>ofGreedy(State::integrate)
                                : Integrator.<State, T, RR>of(State::integrate),
                        (leftCombiner == Gatherer.defaultCombiner()
                                || rightCombiner == Gatherer.defaultCombiner())
                                ? Gatherer.defaultCombiner()
                                : State::joinLeft,
                        (leftFinisher == Gatherer.<A, R>defaultFinisher()
                                && rightFinisher == Gatherer.<AA, RR>defaultFinisher())
                                ? Gatherer.defaultFinisher()
                                : State::finish
                );
            }
        }
    }

    // Public built-in Gatherers and factory methods for them

    /**
     * Gathers elements into fixed-size windows. The last window may contain
     * fewer elements than the supplied window size.
     *
     * <p>Example:
     * {@snippet lang = java:
     * // will contain: [[1, 2, 3], [4, 5, 6], [7, 8]]
     * List<List<Integer>> windows =
     *     Stream.of(1,2,3,4,5,6,7,8).gather(Gatherers.windowFixed(3)).toList();
     *}
     *
     * @param windowSize the size of the windows
     * @param <TR>       the type of elements the returned gatherer consumes
     *                   and the contents of the windows it produces
     * @return a new gatherer which groups elements into fixed-size windows
     * @throws IllegalArgumentException when groupSize is less than 1
     */
    public static <TR> Gatherer<TR, ?, List<TR>> windowFixed(final int windowSize) {
        if (windowSize < 1)
            throw new IllegalArgumentException("'windowSize' must be greater than zero");

        class FixedWindow {
            Object[] window;
            int at;

            FixedWindow() {
                at = 0;
                window = new Object[windowSize];
            }

            boolean integrate(final TR element, final Downstream<? super List<TR>> downstream) {
                window[at++] = element;
                if (at < windowSize) {
                    return true;
                } else {
                    final Object[] oldWindow = window;
                    window = new Object[windowSize];
                    at = 0;
                    return downstream.push(unmodifiableListCopyOf(oldWindow));
                }
            }

            void finish(final Downstream<? super List<TR>> downstream) {
                if (at > 0 && !downstream.isRejecting()) {
                    final Object[] lastWindow = new Object[at];
                    System.arraycopy(window, 0, lastWindow, 0, at);
                    window = null;
                    at = 0;
                    downstream.push(unmodifiableListCopyOf(lastWindow));
                }
            }
        }
        return Gatherer.<TR, FixedWindow, List<TR>>ofSequential(
                // Initializer
                FixedWindow::new,

                // Integrator
                Integrator.<FixedWindow, TR, List<TR>>ofGreedy(FixedWindow::integrate),

                // Finisher
                FixedWindow::finish
        );
    }

    /**
     * Gathers elements into sliding windows, sliding out the most previous
     * element and sliding in the next element for each subsequent window.
     * If the stream is empty then no window will be produced. If the size of
     * the stream is smaller than the window size then only one window will
     * be emitted, containing all elements.
     *
     * <p>Example:
     * {@snippet lang = java:
     * // will contain: [[1, 2], [2, 3], [3, 4], [4, 5], [5, 6], [6, 7], [7, 8]]
     * List<List<Integer>> windows =
     *     Stream.of(1,2,3,4,5,6,7,8).gather(Gatherers.windowSliding(2)).toList();
     *}
     *
     * @param windowSize the size of the windows
     * @param <TR>       the type of elements the returned gatherer consumes
     *                   and the contents of the windows it produces
     * @return a new gatherer which groups elements into sliding windows
     * @throws IllegalArgumentException when windowSize is less than 1
     */
    public static <TR> Gatherer<TR, ?, List<TR>> windowSliding(final int windowSize) {
        if (windowSize < 1)
            throw new IllegalArgumentException("'windowSize' must be greater than zero");

        class SlidingWindow {
            Object[] window;
            int at;
            boolean firstWindow;

            SlidingWindow() {
                firstWindow = true;
                at = 0;
                window = new Object[windowSize];
            }

            boolean integrate(final TR element, final Downstream<? super List<TR>> downstream) {
                window[at++] = element;
                if (at < windowSize) {
                    return true;
                } else {
                    final Object[] oldWindow = window;
                    final Object[] newWindow = new Object[windowSize];
                    System.arraycopy(oldWindow, 1, newWindow, 0, windowSize - 1);
                    window = newWindow;
                    at -= 1;
                    firstWindow = false;
                    return downstream.push(unmodifiableListCopyOf(oldWindow));
                }
            }

            void finish(final Downstream<? super List<TR>> downstream) {
                if (firstWindow && at > 0 && !downstream.isRejecting()) {
                    final Object[] lastWindow = new Object[at];
                    System.arraycopy(window, 0, lastWindow, 0, at);
                    window = null;
                    at = 0;
                    downstream.push(unmodifiableListCopyOf(lastWindow));
                }
            }
        }
        return Gatherer.<TR, SlidingWindow, List<TR>>ofSequential(
                // Initializer
                SlidingWindow::new,
                // Integrator
                Integrator.<SlidingWindow, TR, List<TR>>ofGreedy(SlidingWindow::integrate),
                // Finisher
                SlidingWindow::finish
        );
    }

    @SuppressWarnings("unchecked")
    public static <TR> List<TR> unmodifiableListCopyOf(final Object[] oldWindow) {
        return (List<TR>) Collections.unmodifiableList(new ArrayList<>(Arrays.asList(oldWindow)));
    }

    /**
     * An operation which performs an ordered, <i>reduction-like</i>,
     * transformation for scenarios where no combiner-function can be
     * implemented, or for reductions which are intrinsically
     * order-dependent.
     *
     * <p>This operation always emits a single resulting element.
     *
     * <p>Example:
     * {@code
     * // will contain: Optional[123456789]
     * Optional<String> numberString =
     * Stream.of(1,2,3,4,5,6,7,8,9)
     * .gather(
     * Gatherers.fold(() -> "", (string, number) -> string + number)
     * )
     * .findFirst();
     * }
     *
     * @param initial the identity value for the fold operation
     * @param folder  the folding function
     * @param <T>     the type of elements the returned gatherer consumes
     * @param <R>     the type of elements the returned gatherer produces
     * @return a new Gatherer
     * @throws NullPointerException if any of the parameters are null
     * @see Stream#reduce(Object, BinaryOperator)
     */
    public static <T, R> Gatherer<T, ?, R> fold(
            final Supplier<R> initial,
            final BiFunction<? super R, ? super T, ? extends R> folder) {
        Objects.requireNonNull(initial, "'initial' must not be null");
        Objects.requireNonNull(folder, "'folder' must not be null");

        class State {
            R value = initial.get();

            State() {
            }
        }

        return Gatherer.ofSequential(
                State::new,
                Integrator.ofGreedy((state, element, downstream) -> {
                    state.value = folder.apply(state.value, element);
                    return true;
                }),
                (state, downstream) -> downstream.push(state.value)
        );
    }

    /**
     * Performs a prefix scan -- an incremental accumulation, using the
     * provided functions.
     *
     * @param initial the supplier of the initial value for the scanner
     * @param scanner the function to apply for each element
     * @param <T>     the type of element which this gatherer consumes
     * @param <R>     the type of element which this gatherer produces
     * @return a new Gatherer which performs a prefix scan
     * @throws NullPointerException if any of the parameters are null
     */
    public static <T, R> Gatherer<T, ?, R> scan(
            final Supplier<R> initial,
            final BiFunction<? super R, ? super T, ? extends R> scanner) {
        Objects.requireNonNull(initial, "'initial' must not be null");
        Objects.requireNonNull(scanner, "'scanner' must not be null");

        class State {
            R current = initial.get();

            boolean integrate(final T element, final Downstream<? super R> downstream) {
                return downstream.push(current = scanner.apply(current, element));
            }
        }

        return Gatherer.ofSequential(State::new,
                Integrator.<State, T, R>ofGreedy(State::integrate));
    }
}