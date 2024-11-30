package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.sequences.Sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the neighbors
 *            <p>
 *            T and S must be of same type for this interface to work properly
 */
@FunctionalInterface
public interface Node<T, S extends Node<T, S>> {

    Iterator<S> neighborIterator();

    default Sequence<S> breadthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.breadthFirstIterator((S) this, false));
    }

    default Sequence<S> depthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.depthFirstIterator((S) this, false));
    }

    default Sequence<S> predecessorSequence() {
        //noinspection unchecked
        return () -> predecessorIterator((S) this);
    }

    default Optional<S> optionalPredecessor() {
        throw new IllegalStateException("optionalPredecessor() is not implemented by default. Override it if you want to use it");
    }


    private Iterator<S> predecessorIterator(final S initial) {
        return new Iterator<>() {

            private boolean hasNext = true;
            private S next = initial;

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
            public S next() {
                if (hasNext()) {
                    hasNext = false;
                    return next;
                }
                throw new NoSuchElementException();
            }
        };
    }
}

