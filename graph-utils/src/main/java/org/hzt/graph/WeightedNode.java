package org.hzt.graph;

import org.hzt.utils.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Collections;
import java.util.List;

public interface WeightedNode<T> extends Sequence<WeightedEdge<T>> {

    static <T> WeightedNode<T> of(T payload, int cost) {
        return new ObservableWeightedNodeImpl<>(payload, cost);
    }

    static <T> WeightedNode<T> ofCost(int cost) {
        return of(null, cost);
    }

    T getPayload();

    default int costPlusHeuristic() {
        return getCost() + getHeuristic();
    }

    default int compareTo(WeightedNode<T> other) {
        return costPlusHeuristic() - other.costPlusHeuristic();
    }

    List<WeightedEdge<T>> getWeightedEdges();

    List<WeightedEdge<T>> getEdges();

    @NotNull
    @Override
    default Iterator<WeightedEdge<T>> iterator() {
        return getEdges().iterator();
    }

    default void setPredecessor(Node<T> node) {

    }

    /**
     * @return the predecessor of the node
     */
    WeightedNode<T> getPredecessor();

    /**
     * Set the predecessor of the node. In the graph path search, an algorithm finds the nodes
     * to form possibly the best path (the best is relative, depending on how the algorithm
     * evaluate the cost) between the origin and destination. The search goes node by node
     * from the origin to the destination, for every two consecutive nodes, the leading node
     * is the predecessor of the trailing node.
     *
     * @param node node
     */
    void setPredecessor(WeightedNode<T> node);

    /**
     * @return true if the node is open, false otherwise
     * @see #setOpen
     */
    boolean isOpen();

    /**
     * Makes the node open or closed. When set to closed, it will no longer be considered
     * as a candidate in the graph path search.
     *
     * @param open open
     */
    void setOpen(boolean open);

    /**
     * @return if this node has been visited during the graph path search
     */
    boolean isVisited();

    /**
     * Indicates whether or not the node has been visited during the graph path search.
     *
     * @param visited visited
     */
    void setVisited(boolean visited);

    /**
     * @return true if the node has been selected to be part of the resulted path, false otherwise
     */
    boolean isSelected();

    /**
     * Indicates whether or not the node has been selected to be part of the resulted path.
     *
     * @param selected selected
     */
    void setSelected(boolean selected);

    /**
     * @return the heuristic value of the node
     */
    int getHeuristic();

    /**
     * Set the heuristic value evaluated as the cost from the node to the destination.
     *
     * @param heuristic heuristic
     */
    void setHeuristic(int heuristic);

    /**
     * @return the cost of node
     */
    int getCost();

    /**
     * Set the cost evaluated from the origin to the node.
     *
     * @param cost cost
     */
    void setCost(int cost);

    boolean addEdge(WeightedEdge<T> weightedEdge);
}
