package org.hzt.graph.iterators;

import org.hzt.graph.Node;
import org.hzt.graph.TreeNode;

import java.util.Iterator;

public final class GraphIterators {

    private GraphIterators() {
    }

    public static <T, S extends TreeNode<T, S>> Iterator<S> treeNodeBreadthFirstIterator(final S source) {
        return new TreeNodeBreadthFirstIterator<>(source);
    }

    public static <T, S extends TreeNode<T, S>> Iterator<S> treeNodeDepthFirstIterator(final S source) {
        return new TreeNodeDepthFirstIterator<>(source);
    }

    public static <T, S extends Node<T, S>> Iterator<S> breadthFirstIterator(final S s) {
        return new BreadthFirstIterator<>(s);
    }

    public static <T, S extends Node<T, S>> Iterator<S> depthFirstIterator(final S s) {
        return new DepthFirstIterator<>(s);
    }
}
