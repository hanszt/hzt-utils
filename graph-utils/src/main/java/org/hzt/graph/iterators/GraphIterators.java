package org.hzt.graph.iterators;

import org.hzt.graph.Node;
import org.hzt.graph.TreeNode;
import org.hzt.graph.tuples.DepthToTreeNode;

import java.util.Iterator;

public final class GraphIterators {

    private GraphIterators() {
    }

    public static <T, S extends TreeNode<T, S>> Iterator<S> treeNodeBreadthFirstIterator(S source) {
        return new TreeNodeBreadthFirstIterator<>(source);
    }

    public static <T, S extends TreeNode<T, S>> Iterator<DepthToTreeNode<S>> treeNodeBreadthFirstDepthTrackingIterator(S source) {
        return new TreeNodeBreadthFirstDepthTrackingIterator<>(source);
    }
    public static <T, S extends TreeNode<T, S>> Iterator<S> treeNodeDepthFirstIterator(S source) {
        return new TreeNodeDepthFirstIterator<>(source);
    }

    public static <T, S extends TreeNode<T, S>> Iterator<DepthToTreeNode<S>> treeNodeDepthFirstDepthTrackingIterator(S source) {
        return new TreeNodeDepthFirstDepthTrackingIterator<>(source);
    }

    public static <T, S extends Node<T, S>> Iterator<S> breadthFirstIterator(S s) {
        return new BreadthFirstIterator<>(s);
    }

    public static <T, S extends Node<T, S>>Iterator<S> depthFirstIterator(S s) {
        return new DepthFirstIterator<>(s);
    }
}
