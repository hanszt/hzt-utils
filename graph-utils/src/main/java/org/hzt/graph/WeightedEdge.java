package org.hzt.graph;

public interface WeightedEdge<T> {

    static <T> WeightedEdge<T> from(final WeightedNode<T> from) {
        return new DefaultWeightedEdge<>(from);
    }

    static <T> WeightedEdge<T> of(final WeightedNode<T> weightedNode, final WeightedNode<T> other) {
        return new DefaultWeightedEdge<>(weightedNode).withToNode(other);
    }

    static <T> void edgesInBothDirectionsBetween(final WeightedNode<T> node, final WeightedNode<T> other, final int weight) {
        node.getEdges().add(WeightedEdge.of(node, other).withWeight(weight));
        other.getEdges().add(WeightedEdge.of(other, node).withWeight(weight));
    }

    /**
     * @return node A
     */
    WeightedNode<T> fromNode();


    WeightedEdge<T> withToNode(WeightedNode<T> weightedNode);

    WeightedEdge<T> withWeight(int weight);
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
    default WeightedNode<T> getOpposite(final WeightedNode<T> node) {
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

final class DefaultWeightedEdge<T> implements  WeightedEdge<T> {

    private final WeightedNode<T> from;

    private WeightedNode<T> to;

    private int weight;

    public DefaultWeightedEdge(final WeightedNode<T> from) {
        this.from = from;
    }

    @Override
    public WeightedNode<T> fromNode() {
        return from;
    }

    @Override
    public WeightedEdge<T> withToNode(final WeightedNode<T> weightedNode) {
        to = weightedNode;
        return this;
    }

    @Override
    public WeightedNode<T> toNode() {
        return to;
    }

    @Override
    public WeightedEdge<T> withWeight(final int weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public int weight() {
        return weight;
    }
}
