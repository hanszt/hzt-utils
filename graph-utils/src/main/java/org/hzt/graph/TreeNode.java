package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;

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

    default S setParent(S parent) {
        throw new IllegalStateException("setParent(TreeNode) not supported by default. tried to set " + parent + " as parent");
    }

    default S addChild(S toAdd) {
        final Collection<S> children = getChildren();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addChildren(Iterable<S> toAdd) {
        final Collection<S> children = getChildren();
        for (S child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S addChildAndSetParent(S toAdd) {
        final Collection<S> children = getChildren();
        children.add(toAdd);
        try {
            //noinspection unchecked
            toAdd.setParent((S) this);
        } catch (IllegalStateException e) {
            final String message = "Could not set parent. Override setParent(TreeNode) or try to use addChild(TreeNode) instead...";
            throw new IllegalStateException(message, e);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S addChildrenAndSetParent(Iterable<S> toAdd) {
        final Collection<S> children = getChildren();
        for (S child : toAdd) {
            children.add(child);
            //noinspection unchecked
            child.setParent((S) this);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S removeSubTree(S branch) {
        final Collection<S> branchChildren = branch.getChildren();
        for (S child : branchChildren) {
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

    default Sequence<DepthToTreeNode<S>> breadthFirstDepthTrackingSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeBreadthFirstDepthTrackingIterator((S) this));
    }

    default Sequence<S> depthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeDepthFirstIterator((S) this));
    }

    default Sequence<DepthToTreeNode<S>> depthFirstDepthTrackingSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeDepthFirstDepthTrackingIterator((S) this));
    }

    default Sequence<S> parentSequence() {
        final Iterator<S> iterator = new Iterator<>() {

            S next = null;

            @Override
            public boolean hasNext() {
                final Optional<S> parent = next == null ? optionalParent() : next.optionalParent();
                final boolean present = parent.isPresent();
                if (present) {
                    next = parent.orElseThrow();
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
        final StringBuilder sb = new StringBuilder();
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
