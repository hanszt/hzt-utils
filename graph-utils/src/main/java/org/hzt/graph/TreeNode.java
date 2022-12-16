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
        TreeNodeHelper.map((S) this, function, collection);
        return collection;
    }

    default <R, C extends Collection<R>> C mapLeafsTo(Supplier<C> supplier, Function<? super S, ? extends R> function) {
        final C collection = supplier.get();
        //noinspection unchecked
        TreeNodeHelper.mapLeafs((S) this, function, collection);
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
        final StringBuilder sb = new StringBuilder();
        TreeNodeHelper.toTreeString(this, sb, opening, separator, closing, toStringFunction);
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
        TreeNodeHelper.toTreeString(this, sb, 0, indent, indentString, toStringFunction);
        return sb.toString();
    }
}
