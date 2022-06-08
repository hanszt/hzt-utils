package org.hzt.graph;

public interface WeightedEdge<T> extends Edge<T> {

    static <T> WeightedEdge<T> build(WeightedNode<T> node1, WeightedNode<T> node2, int weight) {
        return new WeightedEdgeImpl<>(node1, node2, weight);
    }

    /**
     * @return node A
     */
    WeightedNode<T> fromNode();

    /**
     * @return node B
     */
    WeightedNode<T> toNode();

    /**
     * Given either node A, or B, return the other node of the two.
     *
     * @param node node
     * @return the other node opposite to the given node
     */
    default WeightedNode<T> getOpposite(WeightedNode<T> node) {
        if (node.equals(fromNode())) {
            return toNode();
        }
        if (node.equals(toNode())) {
            return fromNode();
        }
        throw new IllegalArgumentException("No opposite found for given node");
    }

    /**
     * @return the weight of the edge
     */
    int weight();
}
