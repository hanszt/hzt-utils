package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.utils.sequences.Sequence;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the children
 *
 * T and S must be of same type for this interface to work properly
 */
@FunctionalInterface
public interface TreeNode<T, S extends TreeNode<T, S>> {

    Collection<S> getChildren();

    default boolean isLeaf() {
        return getChildren().isEmpty();
    }

    default int treeDepth() {
        return (int) parentSequence().count();
    }

    default Optional<S> optionalParent() {
        throw new IllegalStateException("optionalParent() is not implemented by default. Override it if you want to use it");
    }

    default S parent() {
        return optionalParent().orElseThrow(() -> new IllegalStateException("No parent found for node: " + this));
    }

    default S setParent(final S parent) {
        throw new IllegalStateException("setParent(TreeNode) not supported by default. tried to set " + parent + " as parent");
    }

    default S addChild(final S toAdd) {
        final Collection<S> children = getChildren();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addChildren(final Iterable<S> toAdd) {
        final Collection<S> children = getChildren();
        for (final S child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S addChildAndSetParent(final S toAdd) {
        final Collection<S> children = getChildren();
        children.add(toAdd);
        try {
            //noinspection unchecked
            toAdd.setParent((S) this);
        } catch (final IllegalStateException e) {
            final String message = "Could not set parent. Override setParent(TreeNode) or try to use addChild(TreeNode) instead...";
            throw new IllegalStateException(message, e);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S addChildrenAndSetParent(final Iterable<S> toAdd) {
        final Collection<S> children = getChildren();
        for (final S child : toAdd) {
            children.add(child);
            //noinspection unchecked
            child.setParent((S) this);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S removeSubTree(final S branch) {
        final Collection<S> branchChildren = branch.getChildren();
        for (final S child : branchChildren) {
            if (!child.isLeaf()) {
                removeSubTree(child);
            }
        }
        branchChildren.removeIf(TreeNode::isLeaf);
        if (branch.isLeaf()) {
            getChildren().removeIf(branch::equals);
        }
        //noinspection unchecked
        return (S) this;
    }

    default Sequence<S> breadthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeBreadthFirstIterator((S) this));
    }

    default Sequence<S> depthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeDepthFirstIterator((S) this));
    }

    default Sequence<S> parentSequence() {
        final Iterator<S> iterator = new Iterator<S>() {

            S next = null;

            @Override
            public boolean hasNext() {
                final Optional<S> parent = next == null ? optionalParent() : next.optionalParent();
                final boolean present = parent.isPresent();
                if (present) {
                    next = parent.orElseThrow(IllegalStateException::new);
                }
                return next != null && present;
            }

            @Override
            public S next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                return next;
            }
        };
        return Sequence.of(() -> iterator);
    }

    default <R, C extends Collection<R>> C mapTo(final Supplier<C> collectionFactory, final Function<? super S, ? extends R> function) {
        final C collection = collectionFactory.get();
        //noinspection unchecked
        TreeNodeHelper.map((S) this, function, collection);
        return collection;
    }

    default <R, C extends Collection<R>> C mapLeafsTo(final Supplier<C> supplier, final Function<? super S, ? extends R> function) {
        final C collection = supplier.get();
        //noinspection unchecked
        TreeNodeHelper.mapLeafs((S) this, function, collection);
        return collection;
    }

    default String toTreeString() {
        return toTreeString(Object::toString);
    }

    default String toTreeString(final Function<? super S, String> toStringFunction) {
        return toTreeString( "[", ", ", "]", toStringFunction);
    }

    default String toTreeString(final String opening, final String separator, final String closing,
                                final Function<? super S, String> toStringFunction) {
        final StringBuilder sb = new StringBuilder();
        TreeNodeHelper.toTreeString(this, sb, opening, separator, closing, toStringFunction);
        return sb.toString();
    }

    default String toTreeString(final int indent) {
        return toTreeString(indent, Object::toString);
    }

    default String toTreeString(final int indent, final Function<? super S, String> toStringFunction) {
        return toTreeString(indent, " ", toStringFunction);
    }

    default String toTreeString(final int indent,
                                final String indentString,
                                final Function<? super S, String> toStringFunction) {
        final StringBuilder sb = new StringBuilder();
        TreeNodeHelper.toTreeString(this, sb, 0, indent, indentString, toStringFunction);
        return sb.toString();
    }
}
