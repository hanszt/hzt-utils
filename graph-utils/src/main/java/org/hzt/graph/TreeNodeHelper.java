package org.hzt.graph;

import org.hzt.utils.strings.StringX;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public final class TreeNodeHelper {

    private TreeNodeHelper() {
    }

    static <T, S extends TreeNode<T, S>> void toTreeString(final TreeNode<T, S> treeNode,
                                                           final StringBuilder sb,
                                                           final int level,
                                                           final int indent,
                                                           final String indentString,
                                                           final Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(StringX.of(indentString).repeat(indent * level))
                .append(toStringFunction.apply((S) treeNode))
                .append("\n");

        final Collection<S> children = treeNode.getChildren();
        if (children.isEmpty()) {
            return;
        }
        for (final S child : children) {
            toTreeString(child, sb, level + 1, indent, indentString, toStringFunction);
        }
    }

    static <T, S extends TreeNode<T, S>> void toTreeString(final TreeNode<T, S> treeNode,
                                                           final StringBuilder sb,
                                                           final String opening,
                                                           final String levelSeparator,
                                                           final String closing,
                                                           final Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(toStringFunction.apply((S) treeNode));
        final Collection<S> children = treeNode.getChildren();
        if (children.isEmpty()) {
            return;
        }
        sb.append(opening);
        for (final Iterator<S> iterator = children.iterator(); iterator.hasNext(); ) {
            final S child = iterator.next();
            toTreeString(child, sb, opening, levelSeparator, closing, toStringFunction);
            if (iterator.hasNext()) {
                sb.append(levelSeparator);
            }
        }
        sb.append(closing);
    }

    static <T, S extends TreeNode<T, S>, R> void map(final S treeNode,
                                                     final Function<? super S, ? extends R> function,
                                                     final Collection<R> collection) {
        final Collection<S> children = treeNode.getChildren();
        collection.add(function.apply(treeNode));
        if (children.isEmpty()) {
            return;
        }
        for (final S child : children) {
            map(child, function, collection);
        }
    }

    static <T, S extends TreeNode<T, S>, R> void mapLeafs(final S treeNode,
                                                          final Function<? super S, ? extends R> function,
                                                          final Collection<R> collection) {
        final Collection<S> children = treeNode.getChildren();
        if (children.isEmpty()) {
            collection.add(function.apply(treeNode));
            return;
        }
        for (final S child : children) {
            mapLeafs(child, function, collection);
        }
    }
}
