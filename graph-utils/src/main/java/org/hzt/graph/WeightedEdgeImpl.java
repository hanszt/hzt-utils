package org.hzt.graph;


import java.util.Objects;

final class WeightedEdgeImpl<T> implements WeightedEdge<T> {
    private final WeightedNode<T> fromNode;
    private final WeightedNode<T> toNode;
    private final int weight;

    WeightedEdgeImpl(WeightedNode<T> fromNode, WeightedNode<T> toNode, int weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }

    public WeightedNode<T> fromNode() {
        return fromNode;
    }

    public WeightedNode<T> toNode() {
        return toNode;
    }

    public int weight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        //noinspection unchecked
        WeightedEdgeImpl<T> that = (WeightedEdgeImpl<T>) obj;
        return Objects.equals(this.fromNode, that.fromNode) &&
                Objects.equals(this.toNode, that.toNode) &&
                this.weight == that.weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromNode, toNode, weight);
    }

    @Override
    public String toString() {
        return "EdgeImpl[" +
                "nodeA=" + fromNode + ", " +
                "nodeB=" + toNode + ", " +
                "weight=" + weight + ']';
    }

}
