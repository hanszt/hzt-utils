package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the children
 *            <p>
 *            T and S must be of same type for this interface to work properly
 *            <p>
 *            The iterator that must be implemented, must provide an iterator over the children of the current node
 */
@FunctionalInterface
public interface TreeNode<T, S extends TreeNode<T, S>> {

    /**
     * @return Returns an iterator that iterates over the children of this tree node
     */
    Iterator<S> childrenIterator();

    default Sequence<S> childrenSequence() {
        return Sequence.of(this::childrenIterator);
    }

    default Sequence<S> siblingSequence() {
        final TreeNode<T, S> noChildrenNode = Collections::emptyIterator;
        //noinspection unchecked
        return optionalParent().orElse((S) noChildrenNode).childrenSequence();
    }

    default boolean isLeaf() {
        return childrenSequence().none();
    }

    default boolean isInternal() {
        return childrenSequence().any();
    }

    default int treeDepth() {
        return (int) parentSequence().count();
    }

    default Collection<S> getMutableChildren() {
        throw new UnsupportedOperationException("getMutableChildren() not supported by default. Override it if you want to use it");
    }

    default Optional<S> optionalParent() {
        throw new IllegalStateException("optionalParent() is not implemented by default. Override it if you want to use it");
    }

    default S withParent(final S parent) {
        throw new IllegalStateException("withParent(TreeNode) not supported by default. Override it if you want to use it. " +
                "Tried to set " + parent + " as parent");
    }

    default S addChild(final S toAdd) {
        final var children = getMutableChildren();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addChildren(final Iterable<S> toAdd) {
        final var children = getMutableChildren();
        for (final var child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S addChildWithThisAsParent(final S toAdd) {
        final var children = getMutableChildren();
        children.add(toAdd);
        try {
            //noinspection unchecked
            toAdd.withParent((S) this);
        } catch (final IllegalStateException e) {
            final var message = "Could not set parent. Override withParent(TreeNode) or try to use addChild(TreeNode) instead...";
            throw new IllegalStateException(message, e);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S addChildrenWithThisAsParent(final Iterable<S> toAdd) {
        final var children = getMutableChildren();
        for (final var child : toAdd) {
            children.add(child);
            //noinspection unchecked
            child.withParent((S) this);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S removeSubTree(final S branch) {
        final var branchChildren = branch.getMutableChildren();
        for (final var child : branchChildren) {
            if (!child.isLeaf()) {
                removeSubTree(child);
            }
        }
        branchChildren.removeIf(TreeNode::isLeaf);
        if (branch.isLeaf()) {
            getMutableChildren().removeIf(branch::equals);
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
        //noinspection unchecked
        final var initial = (S) this;
        final Iterator<S> iterator = new Iterator<>() {
            boolean isThis = true;
            S next = initial;

            @Override
            public boolean hasNext() {
                if (isThis) {
                    isThis = false;
                    return true;
                }
                final var optionalParent = next.optionalParent();
                final var present = optionalParent.isPresent();
                if (present) {
                    next = optionalParent.orElseThrow();
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

    default String toTreeString() {
        return toTreeString(Object::toString);
    }

    default String toTreeString(final Function<? super S, String> toStringFunction) {
        return toTreeString("[", ", ", "]", toStringFunction);
    }

    default String toTreeString(final String opening, final String separator, final String closing,
                                final Function<? super S, String> toStringFunction) {
        final var sb = new StringBuilder();
        toTreeString(this, sb, opening, separator, closing, toStringFunction);
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
        final var sb = new StringBuilder();
        toTreeString(this, sb, 0, indent, indentString, toStringFunction);
        final var sbNoTrailingWhiteSpace = sb.replace(sb.length() - 1, sb.length(), "");
        return sbNoTrailingWhiteSpace.toString();
    }

    default String toBFSTreeString(final int indent) {
        return toBFSTreeString(indent, Object::toString);
    }

    default String toBFSTreeString(final int indent, final Function<? super S, String> toStringFunction) {
        return toBFSTreeString(indent, " ", toStringFunction);
    }

    default String toBFSTreeString(final int indent,
                                   final String indentString,
                                   final Function<? super S, String> toStringFunction) {
        return breadthFirstDepthTrackingSequence()
                .map(n -> indentString.repeat(n.treeDepth() * indent) + toStringFunction.apply(n.node()))
                .joinToString("\n");
    }

    private static <T, S extends TreeNode<T, S>> void toTreeString(final TreeNode<T, S> treeNode,
                                                                   final StringBuilder sb,
                                                                   final int level,
                                                                   final int indent,
                                                                   final String indentString,
                                                                   final Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(StringX.of(indentString).repeat(indent * level))
                .append(toStringFunction.apply((S) treeNode))
                .append("\n");
        if (treeNode.childrenSequence().none()) {
            return;
        }
        for (final var child : treeNode.childrenSequence()) {
            toTreeString(child, sb, level + 1, indent, indentString, toStringFunction);
        }
    }

    private static <T, S extends TreeNode<T, S>> void toTreeString(final TreeNode<T, S> treeNode,
                                                                   final StringBuilder sb,
                                                                   final String opening,
                                                                   final String levelSeparator,
                                                                   final String closing,
                                                                   final Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(toStringFunction.apply((S) treeNode));
        final var iterator = treeNode.childrenIterator();
        if (!iterator.hasNext()) {
            return;
        }
        sb.append(opening);
        while (iterator.hasNext()) {
            final var child = iterator.next();
            toTreeString(child, sb, opening, levelSeparator, closing, toStringFunction);
            if (iterator.hasNext()) {
                sb.append(levelSeparator);
            }
        }
        sb.append(closing);
    }
}
