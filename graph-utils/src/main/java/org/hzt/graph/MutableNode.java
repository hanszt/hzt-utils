package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.sequences.Sequence;

/**
 * @param <N> The type of the node
 */
public interface MutableNode<N extends Node<N>> extends Node<N> {

    MutableCollectionX<N> getMutableNeighbors();

    default N addNeighbor(final N toAdd) {
        final var children = getMutableNeighbors();
        children.add(toAdd);
        //noinspection unchecked
        return (N) this;
    }

    default N addNeighbors(final Iterable<N> toAdd) {
        final var children = getMutableNeighbors();
        for (final var child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (N) this;
    }

    default N bidiAddNeighbor(final N toAdd) {
        if (shouldThrowIfNeighborCanNotBeAdded() && toAdd == null) {
            throw new IllegalStateException("Neighbor was null!");
        }
        if (toAdd != null) {
            final var neighbors = getMutableNeighbors();
            neighbors.add(toAdd);
            if (toAdd instanceof MutableNode<?> mutToAdd) {
                //noinspection unchecked
                ((MutableNode<N>) mutToAdd).getMutableNeighbors().add((N) this);
            } else {
                throw new IllegalStateException(toAdd + " is not an instance of MutableNode");
            }
        }
        //noinspection unchecked
        return (N) this;
    }

    default N bidiAddNeighbors(final Iterable<N> toAdd) {
        final var neighbors = getMutableNeighbors();
        for (final var neighbor : toAdd) {
            if (shouldThrowIfNeighborCanNotBeAdded() && neighbor == null) {
                throw new IllegalStateException("One of the neighbors in [" + Sequence.of(toAdd).joinToString() + "] was null!");
            }
            if (neighbor != null) {
                neighbors.add(neighbor);
                if (neighbor instanceof MutableNode<?> mutNeighbor) {
                    //noinspection unchecked
                    ((MutableNode<N>) mutNeighbor).getMutableNeighbors().add((N) this);
                } else {
                    throw new IllegalStateException(toAdd + " is not an instance of MutableNode");
                }
            }
        }
        //noinspection unchecked
        return (N) this;
    }

    default boolean shouldThrowIfNeighborCanNotBeAdded() {
        return true;
    }

    default Sequence<N> breadthFirstSequence(final Mode mode) {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.breadthFirstIterator((N) this, mode == Mode.SET_PREDECESSORS));
    }

    default Sequence<N> depthFirstSequence(final Mode mode) {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.depthFirstIterator((N) this, mode == Mode.SET_PREDECESSORS));
    }

    /**
     * Set the predecessor of the node. In the graph path search, an algorithm finds the nodes
     * to form possibly the best path between the origin and destination. The search goes node by node
     * from the origin to the destination, for every two consecutive nodes, the leading node
     * is the predecessor of the trailing node.
     *
     * @param predecessor node
     */
    default N withPredecessor(final N predecessor) {
        throw new IllegalStateException("withPredecessor(Node) not supported by default. Override it if you want to use it. " +
                "Tried to set " + predecessor + " as predecessor");
    }

    enum Mode {
        SET_PREDECESSORS, NO_PREDECESSOR
    }
}

