package org.hzt.graph;


import java.util.Iterator;
import java.util.List;

public interface WeightedNode<T> extends Node<WeightedNode<T>, WeightedNode<T>> {

    static <T> WeightedNode<T> of(final T payload) {
        return new WeightedNode<>() {
            @Override
            public Iterator<WeightedNode<T>> neighborIterator() {
                return null;
            }

            @Override
            public T getPayload() {
                return payload;
            }

            @Override
            public List<WeightedEdge<T>> getEdges() {
                throw new IllegalStateException();
            }

            @Override
            public void setCost(final int i) {
                // not implemented
            }

            @Override
            public int getCost() {
                return 0;
            }
        };
    }

    T getPayload();

    List<WeightedEdge<T>> getEdges();

    @Override
    default List<WeightedNode<T>> getMutableNeighbors() {
        return getEdges().stream().map(e -> e.getOpposite(this)).toList();
    }

    default Iterator<WeightedEdge<T>> edgeIterator() {
        return getEdges().iterator();
    }

    default WeightedNode<T> addEdgeTo(final WeightedNode<T> other, final int weight) {
        getEdges().add(WeightedEdge.of(this, other).withWeight(weight));
        return this;
    }

    int getCost();

    void setCost(int i);

}
