package org.hzt.graph;

import java.util.ArrayList;
import java.util.List;

public class NodeImpl<T> implements Node<T> {

    private final T payload;
    private final List<NodeImpl<T>> neighbors = new ArrayList<>();

    public NodeImpl(T payload) {
        this.payload = payload;
    }

    public List<NodeImpl<T>> getNeighbors() {
        return List.copyOf(neighbors);
    }

    public T getPayload() {
        return payload;
    }
}
