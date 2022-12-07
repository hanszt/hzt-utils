package org.hzt.graph;

import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the children
 */
public interface TreeNode<T, S extends TreeNode<T, S>> extends Iterable<S> {

    default T getParent() {
        throw new UnsupportedOperationException("getParent() is not implemented by default");
    }

    Collection<S> getChildren();

    default S addChildren(Iterable<S> toAdd) {
        final Collection<S> children = getChildren();
        for (S child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default Iterator<S> iterator() {
        return mapTo(ArrayList::new, It::self).iterator();
    }

    default Sequence<S> asSequence() {
        return Sequence.of(this);
    }

    default <R, C extends Collection<R>> C mapTo(Supplier<C> collectionFactory, Function<? super S, ? extends R> function) {
        final C collection = collectionFactory.get();
        //noinspection unchecked
        map((S) this, function, collection);
        return collection;
    }

    default <R, C extends Collection<R>> C mapLeafsTo(Supplier<C> supplier, Function<? super S, ? extends R> function) {
        final C collection = supplier.get();
        //noinspection unchecked
        mapLeafs((S) this, function, collection);
        return collection;
    }

    default String toTreeString(int indent) {
        final StringBuilder sb = new StringBuilder();
        toTreeString(this, 0, sb, indent);
        return sb.toString();
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
