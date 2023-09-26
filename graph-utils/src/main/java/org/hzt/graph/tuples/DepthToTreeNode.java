package org.hzt.graph.tuples;

public record DepthToTreeNode<T>(int treeDepth, T node) {

    @Override
    public String toString() {
        return "DepthToTreeNode{" +
                "treeDepth=" + treeDepth +
                ", node=" + node +
                '}';
    }
}
