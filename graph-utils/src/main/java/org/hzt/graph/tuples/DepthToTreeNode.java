package org.hzt.graph.tuples;

import java.util.Objects;

public final class DepthToTreeNode<T> {
    private final int treeDepth;
    private final T node;

    public DepthToTreeNode(final int treeDepth, final T node) {
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
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        final DepthToTreeNode<?> that = (DepthToTreeNode<?>) obj;
        return this.treeDepth == that.treeDepth &&
               Objects.equals(this.node, that.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treeDepth, node);
    }

    @Override
    public String toString() {
        return "DepthToTreeNode[" +
               "treeDepth=" + treeDepth + ", " +
               "node=" + node + ']';
    }

}
