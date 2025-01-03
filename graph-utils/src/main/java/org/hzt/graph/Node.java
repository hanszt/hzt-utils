package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.sequences.Sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @param <N> The type of the neighbors
 */
@FunctionalInterface
public interface Node<N extends Node<N>> {

    Iterator<N> neighborIterator();

    default Sequence<N> breadthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.breadthFirstIterator((N) this, false));
    }

    default Sequence<N> depthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.depthFirstIterator((N) this, false));
    }

    default Sequence<N> predecessorSequence() {
        //noinspection unchecked
        return () -> predecessorIterator((N) this);
    }

    default Optional<N> optionalPredecessor() {
        throw new IllegalStateException("optionalPredecessor() is not implemented by default. Override it if you want to use it");
    }

    private Iterator<N> predecessorIterator(final N initial) {
        return new Iterator<>() {

            private boolean hasNext = true;
            private N next = initial;

            @Override
            public boolean hasNext() {
                if (hasNext) {
                    return true;
                }
                final var predecessor = next.optionalPredecessor();
                hasNext = predecessor.isPresent();
                if (hasNext) {
                    next = predecessor.orElseThrow();
                }
                return hasNext;
            }

            @Override
            public N next() {
                if (hasNext()) {
                    hasNext = false;
                    return next;
                }
                throw new NoSuchElementException();
            }
        };
    }
}

