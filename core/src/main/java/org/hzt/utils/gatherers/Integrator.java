package org.hzt.utils.gatherers;

/**
 * An Integrator receives elements and processes them,
 * optionally using the supplied state, and optionally sends incremental
 * results downstream.
 *
 * @param <A> the type of state used by this integrator
 * @param <T> the type of elements this integrator consumes
 * @param <R> the type of results this integrator can produce
 */
@FunctionalInterface
public interface Integrator<A, T, R> {
    /**
     * Integrate is the method which given:
     * the current state, the next element, and a downstream object;
     * performs the main logic -- potentially inspecting and/or updating
     * the state, optionally sending any number of elements downstream
     * -- and then returns whether more elements are to be consumed or not.
     *
     * @param state      The state to integrate into
     * @param element    The element to integrate
     * @param downstream The downstream object of this integration,
     *                   returns false if doesn't want any more elements
     * @return {@code true} if subsequent integration is desired,
     * {@code false} if not
     */
    boolean integrate(A state, T element, Gatherer.Downstream<? super R> downstream);

    /**
     * Factory method for turning Integrator-shaped lambdas into
     * Integrators.
     *
     * @param integrator a lambda to create as Integrator
     * @param <A>        the type of elements this integrator receives
     * @param <T>        the type of initializer this integrator consumes
     * @param <R>        the type of results this integrator can produce
     * @return the given lambda as an Integrator
     */
    static <A, T, R> Integrator<A, T, R> of(final Integrator<A, T, R> integrator) {
        return integrator;
    }

    /**
     * Factory method for turning Integrator-shaped lambdas into
     * {@link Greedy} Integrators.
     *
     * @param greedy a lambda to create as Integrator.Greedy
     * @param <A>    the type of elements this integrator receives
     * @param <T>    the type of initializer this integrator consumes
     * @param <R>    the type of results this integrator can produce
     * @return the given lambda as a Greedy Integrator
     */
    static <A, T, R> Greedy<A, T, R> ofGreedy(final Greedy<A, T, R> greedy) {
        return greedy;
    }

    /**
     * Greedy Integrators consume all their input, and may only relay that
     * the downstream does not want more elements.
     *
     * <p>This is used to clarify that no short-circuiting will be
     * initiated by this Integrator, and that information can then be
     * used to optimize evaluation.
     *
     * @param <A> the type of elements this greedy integrator receives
     * @param <T> the type of initializer this greedy integrator consumes
     * @param <R> the type of results this greedy integrator can produce
     */
    @FunctionalInterface
    public interface Greedy<A, T, R> extends Integrator<A, T, R> {
    }
}
