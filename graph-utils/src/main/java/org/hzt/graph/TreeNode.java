package org.hzt.graph;

import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the children
 *
 * In practice, T and S should be the same
 */
@FunctionalInterface
public interface TreeNode<T, S extends TreeNode<T, S>> {

    Collection<S> getChildren();

    default boolean isLeaf() {
        return getChildren().isEmpty();
    }

    default S addChildren(Iterable<S> toAdd) {
        final Collection<S> children = getChildren();
        for (S child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default Iterator<S> treeIterator() {
        return mapTo(ArrayList::new, It::self).iterator();
    }

    default Sequence<S> asSequence() {
        return Sequence.of(this::treeIterator);
    }

    default Stream<S> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(treeIterator(), 0), false);
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

    default String toTreeString() {
        return toTreeString(Object::toString);
    }

    default String toTreeString(Function<? super S, String> toStringFunction) {
        return toTreeString( "[", ", ", "]", toStringFunction);
    }

    default String toTreeString(String opening, String separator, String closing,
                                Function<? super S, String> toStringFunction) {
        final var sb = new StringBuilder();
        toTreeString(this, sb, opening, separator, closing, toStringFunction);
        return sb.toString();
    }

    default String toTreeString(int indent) {
        return toTreeString(indent, Object::toString);
    }

    default String toTreeString(int indent, Function<? super S, String> toStringFunction) {
        return toTreeString(indent, " ", toStringFunction);
    }

    default String toTreeString(int indent,
                                String indentString,
                                Function<? super S, String> toStringFunction) {
        final StringBuilder sb = new StringBuilder();
        toTreeString(this, sb, 0, indent, indentString, toStringFunction);
        return sb.toString();
    }

    private static <T, S extends TreeNode<T, S>> void toTreeString(TreeNode<T, S> treeNode,
                                                                   StringBuilder sb,
                                                                   int level,
                                                                   int indent,
                                                                   String indentString,
                                                                   Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(indentString.repeat(indent * level))
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

    private static <T, S extends TreeNode<T, S>> void toTreeString(TreeNode<T, S> treeNode,
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

    private static <T, S extends TreeNode<T, S>, R> void map(S treeNode,
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

    private static <T, S extends TreeNode<T, S>, R> void mapLeafs(S treeNode,
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
