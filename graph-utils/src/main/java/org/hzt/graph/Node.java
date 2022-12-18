package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the neighbors
 *
 * T and S must be of same type for this interface to work properly
 */
public interface Node<T, S extends Node<T, S>> {

    Collection<S> getNeighbors();

    default S addNeighbor(S toAdd) {
        final Collection<S> children = getNeighbors();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addNeighbors(Iterable<S> toAdd) {
        final Collection<S> children = getNeighbors();
        for (S child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S bidiAddNeighbor(S toAdd) {
        final Collection<S> neighbors = getNeighbors();
        neighbors.add(toAdd);
        //noinspection unchecked
        toAdd.getNeighbors().add((S) this);
        //noinspection unchecked
        return (S) this;
    }

    default S bidiAddNeighbors(Iterable<S> toAdd) {
        final Collection<S> neighbors = getNeighbors();
        for (S neighbor : toAdd) {
            neighbors.add(neighbor);
            //noinspection unchecked
            neighbor.getNeighbors().add((S) this);
        }
        //noinspection unchecked
        return (S) this;
    }

    default Sequence<S> breadthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.breadthFirstIterator((S) this));
    }

    default Sequence<S> depthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.depthFirstIterator((S) this));
    }

    /**
     * Set the predecessor of the node. In the graph path search, an algorithm finds the nodes
     * to form possibly the best path between the origin and destination. The search goes node by node
     * from the origin to the destination, for every two consecutive nodes, the leading node
     * is the predecessor of the trailing node.
     *
     * @param predecessor node
     */
    S withPredecessor(S predecessor);

    default Sequence<S> predecessorSequence() {
        //noinspection unchecked
        return () -> NodeHelper.predecessorIterator((S) this);
    }

    /**
     * @return the predecessor of the node
     */
    S getPredecessor();

    default <R, C extends Collection<R>> C mapTo(Supplier<C> collectionFactory, Function<? super S, ? extends R> function) {
        final C collection = collectionFactory.get();
        //noinspection unchecked
        NodeHelper.map((S) this, function, collection);
        return collection;
    }
}

