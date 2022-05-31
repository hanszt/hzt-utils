package org.hzt.graph;

public interface Edge<T> {

    Node<T> getFromNode();

    Node<T> getToNode();
}
