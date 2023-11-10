package org.hzt.graph;


import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public interface WeightedNode<T> extends Node<WeightedNode<T>, WeightedNode<T>> {

    static <T> WeightedNode<T> of(final T payload) {
        return new WeightedNode<T>() {
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

            }

            @Override
            public int getCost() {
                return 0;
            }

            @Override
            public WeightedNode<T> withPredecessor(final WeightedNode<T> predecessor) {
                return null;
            }

            @Override
            public WeightedNode<T> getPredecessor() {
                throw new IllegalStateException();
            }
        };
    }

    T getPayload();

    List<WeightedEdge<T>> getEdges();

    default List<WeightedNode<T>> getNeighbors() {
        return getEdges().stream().map(e -> e.getOpposite(this)).collect(Collectors.toList());
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
