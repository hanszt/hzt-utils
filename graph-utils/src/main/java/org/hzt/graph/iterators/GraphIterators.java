package org.hzt.graph.iterators;

import org.hzt.graph.Node;
import org.hzt.graph.TreeNode;
import org.hzt.graph.tuples.DepthToTreeNode;

import java.util.Iterator;

public final class GraphIterators {

    private GraphIterators() {
    }

    public static <T, S extends TreeNode<T, S>> Iterator<S> treeNodeBreadthFirstIterator(final S source) {
        return new TreeNodeBreadthFirstIterator<>(source);
    }

    public static <T, S extends TreeNode<T, S>> Iterator<DepthToTreeNode<S>> treeNodeBreadthFirstDepthTrackingIterator(final S source) {
        return new TreeNodeBreadthFirstDepthTrackingIterator<>(source);
    }
    public static <T, S extends TreeNode<T, S>> Iterator<S> treeNodeDepthFirstIterator(final S source) {
        return new TreeNodeDepthFirstIterator<>(source);
    }

    public static <T, S extends TreeNode<T, S>> Iterator<DepthToTreeNode<S>> treeNodeDepthFirstDepthTrackingIterator(final S source) {
        return new TreeNodeDepthFirstDepthTrackingIterator<>(source);
    }

    public static <T, S extends Node<T, S>> Iterator<S> breadthFirstIterator(final S s, final boolean setPredecessor) {
        return new BreadthFirstIterator<>(s, setPredecessor);
    }

    public static <T, S extends Node<T, S>>Iterator<S> depthFirstIterator(final S s, final boolean setPredecessor) {
        return new DepthFirstIterator<>(s, setPredecessor);
    }
}
