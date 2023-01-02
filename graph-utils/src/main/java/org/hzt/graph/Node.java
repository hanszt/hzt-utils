package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the neighbors
 *
 * T and S must be of same type for this interface to work properly
 */
@FunctionalInterface
public interface Node<T, S extends Node<T, S>> {

    Iterator<S> neighborIterator();

    default Collection<S> getMutableNeighbors() {
        throw new UnsupportedOperationException("getMutableNeighbors() not supported by default. Implement it to use it");
    }

    default S addNeighbor(S toAdd) {
        final Collection<S> children = getMutableNeighbors();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addNeighbors(Iterable<S> toAdd) {
        final Collection<S> children = getMutableNeighbors();
        for (S child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S bidiAddNeighbor(S toAdd) {
        final Collection<S> neighbors = getMutableNeighbors();
        neighbors.add(toAdd);
        //noinspection unchecked
        toAdd.getMutableNeighbors().add((S) this);
        //noinspection unchecked
        return (S) this;
    }

    default S bidiAddNeighbors(Iterable<S> toAdd) {
        final Collection<S> neighbors = getMutableNeighbors();
        for (S neighbor : toAdd) {
            neighbors.add(neighbor);
            //noinspection unchecked
            neighbor.getMutableNeighbors().add((S) this);
        }
        //noinspection unchecked
        return (S) this;
    }

    default Sequence<S> breadthFirstSequence(Mode mode) {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.breadthFirstIterator((S) this, mode == Mode.SET_PREDECESSORS));
    }

    default Sequence<S> breadthFirstSequence() {
        return breadthFirstSequence(Mode.NO_PREDECESSOR);
    }

    default Sequence<S> depthFirstSequence(Mode mode) {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.depthFirstIterator((S) this, mode == Mode.SET_PREDECESSORS));
    }

    default Sequence<S> depthFirstSequence() {
        return depthFirstSequence(Mode.NO_PREDECESSOR);
    }

    enum Mode {
        SET_PREDECESSORS, NO_PREDECESSOR
    }

    /**
     * Set the predecessor of the node. In the graph path search, an algorithm finds the nodes
     * to form possibly the best path between the origin and destination. The search goes node by node
     * from the origin to the destination, for every two consecutive nodes, the leading node
     * is the predecessor of the trailing node.
     *
     * @param predecessor node
     */
    default S withPredecessor(S predecessor) {
        throw new IllegalStateException("withPredecessor(Node) not supported by default. Override it if you want to use it. " +
                "Tried to set " + predecessor + " as predecessor");
    }

    default Sequence<S> predecessorSequence() {
        //noinspection unchecked
        return () -> predecessorIterator((S) this);
    }

    default Optional<S> optionalPredecessor() {
        throw new IllegalStateException("optionalPredecessor() is not implemented by default. Override it if you want to use it");
    }


    private Iterator<S> predecessorIterator(S initial) {
        return new Iterator<>() {

            private boolean isThis = true;
            private S next = initial;

            @Override
            public boolean hasNext() {
                if (isThis) {
                    isThis = false;
                    return true;
                }
                final Optional<S> predecessor = next.optionalPredecessor();
                final boolean present = predecessor.isPresent();
                if (present) {
                    next = predecessor.orElseThrow();
                }
                return next != null && present;
            }

            @Override
            public S next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                return next;
            }
        };
    }
}

