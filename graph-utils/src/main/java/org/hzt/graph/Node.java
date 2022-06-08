package org.hzt.graph;

import org.hzt.utils.sequences.Sequence;

import java.util.Iterator;
import java.util.List;

public interface Node<T> extends Sequence<Edge<T>> {

    T getPayload();

    default Iterator<Edge<T>> iterator() {
        return getEdges().iterator();
    }

    /**
     * @return the edges through which this node is connected to other nodes.
     */
    List<Edge<T>> getEdges();

    /**
     * @return the predecessor of the node
     */
    Node<T> getPredecessor();

    /**
     * Set the predecessor of the node. In the graph path search, an algorithm finds the nodes
     * to form possibly the best path (the best is relative, depending on how the algorithm
     * evaluate the cost) between the origin and destination. The search goes node by node
     * from the origin to the destination, for every two consecutive nodes, the leading node
     * is the predecessor of the trailing node.
     *
     * @param node node
     */
    void setPredecessor(Node<T> node);

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
     * Indicates whether the node has been selected to be part of the resulted path.
     *
     * @param selected selected
     */
    void setSelected(boolean selected);

    boolean addEdge(Edge<T> weightedEdge);
}
