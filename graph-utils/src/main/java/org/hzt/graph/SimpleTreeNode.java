package org.hzt.graph;

import org.hzt.utils.sequences.Sequence;

import java.util.function.Function;

public sealed interface SimpleTreeNode<T> extends TreeNode<SimpleTreeNode<T>> permits InternalTreeNode, LeafTreeNode {

    static <T> InternalTreeNode<T> parse(String s, Delimiters delimiters, Function<String, T> function) {
        return InternalTreeNode.parse(s, delimiters, function);
    }

    default Sequence<T> leafValues() {
        return breadthFirstSequence().mapNotNull(s -> s instanceof LeafTreeNode(T value) ? value : null);
    }

    record Delimiters(String opening, String separator, String closing) {
    }
}
