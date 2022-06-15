package org.hzt.graph;

public interface Edge<T> {

    /**
     * @return node A
     */
    Node<T> fromNode();

    /**
     * @return node B
     */
    Node<T> toNode();

    /**
     * Given either node A, or B, return the other node of the two.
     *
     * @param node node
     * @return the other node opposite to the given node
     */
    default Node<T> getOpposite(Node<T> node) {
        if (node.equals(fromNode())) {
            return toNode();
        }
        if (node.equals(toNode())) {
            return fromNode();
        }
        throw new IllegalArgumentException("No opposite found for given node");
    }
}
