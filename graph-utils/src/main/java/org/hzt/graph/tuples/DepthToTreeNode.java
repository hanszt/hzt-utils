package org.hzt.graph.tuples;

import java.util.Objects;

public final class DepthToTreeNode<T> {

    private final int treeDepth;
    private final T node;

    public DepthToTreeNode(int treeDepth, T node) {
        this.treeDepth = treeDepth;
        this.node = node;
    }

    public int treeDepth() {
        return treeDepth;
    }

    public T node() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (DepthToTreeNode<?>) o;
        return treeDepth == that.treeDepth && Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treeDepth, node);
    }

    @Override
    public String toString() {
        return "DepthToTreeNode{" +
                "treeDepth=" + treeDepth +
                ", node=" + node +
                '}';
    }
}
