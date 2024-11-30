package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.sequences.Sequence;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the neighbors
 *            <p>
 *            T and S must be of same type for this interface to work properly
 */
public interface MutableNode<T, S extends Node<T, S>> extends Node<T, S> {

    MutableCollectionX<S> getMutableNeighbors();

    default S addNeighbor(final S toAdd) {
        final var children = getMutableNeighbors();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addNeighbors(final Iterable<S> toAdd) {
        final var children = getMutableNeighbors();
        for (final var child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S bidiAddNeighbor(final S toAdd) {
        if (shouldThrowIfNeighborCanNotBeAdded() && toAdd == null) {
            throw new IllegalStateException("Neighbor was null!");
        }
        if (toAdd != null) {
            final var neighbors = getMutableNeighbors();
            neighbors.add(toAdd);
            if (toAdd instanceof MutableNode<?, ?> mutToAdd) {
                //noinspection unchecked
                ((MutableNode<T, S>) mutToAdd).getMutableNeighbors().add((S) this);
            } else {
                throw new IllegalStateException(toAdd + " is not an instance of MutableNode");
            }
        }
        //noinspection unchecked
        return (S) this;
    }

    default S bidiAddNeighbors(final Iterable<S> toAdd) {
        final var neighbors = getMutableNeighbors();
        for (final var neighbor : toAdd) {
            if (shouldThrowIfNeighborCanNotBeAdded() && neighbor == null) {
                throw new IllegalStateException("One of the neighbors in [" + Sequence.of(toAdd).joinToString() + "] was null!");
            }
            if (neighbor != null) {
                neighbors.add(neighbor);
                if (neighbor instanceof MutableNode<?, ?> mutNeighbor) {
                    //noinspection unchecked
                    ((MutableNode<T, S>) mutNeighbor).getMutableNeighbors().add((S) this);
                } else {
                    throw new IllegalStateException(toAdd + " is not an instance of MutableNode");
                }
            }
        }
        //noinspection unchecked
        return (S) this;
    }

    default boolean shouldThrowIfNeighborCanNotBeAdded() {
        return true;
    }

    default Sequence<S> breadthFirstSequence(final Mode mode) {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.breadthFirstIterator((S) this, mode == Mode.SET_PREDECESSORS));
    }

    default Sequence<S> depthFirstSequence(final Mode mode) {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.depthFirstIterator((S) this, mode == Mode.SET_PREDECESSORS));
    }

    /**
     * Set the predecessor of the node. In the graph path search, an algorithm finds the nodes
     * to form possibly the best path between the origin and destination. The search goes node by node
     * from the origin to the destination, for every two consecutive nodes, the leading node
     * is the predecessor of the trailing node.
     *
     * @param predecessor node
     */
    default S withPredecessor(final S predecessor) {
        throw new IllegalStateException("withPredecessor(Node) not supported by default. Override it if you want to use it. " +
                "Tried to set " + predecessor + " as predecessor");
    }

    enum Mode {
        SET_PREDECESSORS, NO_PREDECESSOR
    }
}

