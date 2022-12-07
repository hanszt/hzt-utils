package org.hzt.graph;

import org.hzt.utils.strings.StringX;

import java.util.Collection;
import java.util.function.Function;

public final class TreeNodeHelper {

    private TreeNodeHelper() {
    }

    static <T, S extends TreeNode<T, S>> void toTreeString(TreeNode<T, S> treeNode, int level,
                                                           StringBuilder stringBuilder, int indent) {
        final Collection<S> children = treeNode.getChildren();
        stringBuilder
                .append(StringX.of(" ").repeat(level * indent))
                .append(treeNode)
                .append("\n");
        if (children.isEmpty()) {
            return;
        }
        for (S child : children) {
            toTreeString(child, level + 1, stringBuilder, indent);
        }
    }

    static <T, S extends TreeNode<T, S>, R> void map(S treeNode,
                                                     Function<? super S, ? extends R> function,
                                                     Collection<R> collection) {
        final Collection<S> children = treeNode.getChildren();
        collection.add(function.apply(treeNode));
        if (children.isEmpty()) {
            return;
        }
        for (S child : children) {
            map(child, function, collection);
        }
    }

    static <T, S extends TreeNode<T, S>, R> void mapLeafs(S treeNode,
                                                          Function<? super S, ? extends R> function,
                                                          Collection<R> collection) {
        final Collection<S> children = treeNode.getChildren();
        if (children.isEmpty()) {
            collection.add(function.apply(treeNode));
            return;
        }
        for (S child : children) {
            mapLeafs(child, function, collection);
        }
    }
}
