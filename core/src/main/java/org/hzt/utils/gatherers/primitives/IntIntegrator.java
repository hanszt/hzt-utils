package org.hzt.utils.gatherers.primitives;

import java.util.stream.Gatherer;

@FunctionalInterface
public interface IntIntegrator<A, R> {

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
     *                   returns false if it doesn't want any more elements
     * @return {@code true} if subsequent integration is desired,
     * {@code false} if not
     */
    boolean integrate(A state, int element, Gatherer.Downstream<? super R> downstream);
}
