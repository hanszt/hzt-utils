package org.hzt.graph;

public class WeightedEdge<T> implements Edge<T> {

    private final int weight;

    private final WeightedNode<T> fromNode;
    private final WeightedNode<T> toNode;

    public WeightedEdge(int weight, WeightedNode<T> fromNode, WeightedNode<T> toNode) {
        this.weight = weight;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public int getWeight() {
        return weight;
    }

    public WeightedNode<T> getFromNode() {
        return fromNode;
    }

    public WeightedNode<T> getToNode() {
        return toNode;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "weight=" + weight +
                '}';
    }
}
