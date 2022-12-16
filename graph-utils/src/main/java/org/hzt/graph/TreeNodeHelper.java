package org.hzt.graph;

import org.hzt.utils.strings.StringX;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public final class TreeNodeHelper {

    private TreeNodeHelper() {
    }

    static <T, S extends TreeNode<T, S>> void toTreeString(TreeNode<T, S> treeNode,
                                                                   StringBuilder sb,
                                                                   int level,
                                                                   int indent,
                                                                   String indentString,
                                                                   Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(StringX.of(indentString).repeat(indent * level))
                .append(toStringFunction.apply((S) treeNode))
                .append("\n");

        final Collection<S> children = treeNode.getChildren();
        if (children.isEmpty()) {
            return;
        }
        for (S child : children) {
            toTreeString(child, sb, level + 1, indent, indentString, toStringFunction);
        }
    }

    static <T, S extends TreeNode<T, S>> void toTreeString(TreeNode<T, S> treeNode,
                                                                   StringBuilder sb,
                                                                   String opening,
                                                                   String levelSeparator,
                                                                   String closing,
                                                                   Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(toStringFunction.apply((S) treeNode));
        final Collection<S> children = treeNode.getChildren();
        if (children.isEmpty()) {
            return;
        }
        sb.append(opening);
        for (Iterator<S> iterator = children.iterator(); iterator.hasNext(); ) {
            S child = iterator.next();
            toTreeString(child, sb, opening, levelSeparator, closing, toStringFunction);
            if (iterator.hasNext()) {
                sb.append(levelSeparator);
            }
        }
        sb.append(closing);
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
