package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the children
 * <p>
 * T and S must be of same type for this interface to work properly
 * <p>
 * The iterator that must be implemented, must provide an iterator over the children of the current node
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

    /**
     * @return a sequence of siblings including self
     */
    default Sequence<S> siblingSequence() {
        //noinspection unchecked
        return optionalParent()
                .map(TreeNode::childrenSequence)
                .orElse(Sequence.of((S) this));
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

    default Optional<S> optionalParent() {
        throw new IllegalStateException("optionalParent() is not implemented by default. Override it if you want to use it");
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

    /**
     * @return a sequence containing this and the parents of this tree node
     */
    default Sequence<S> parentSequence() {
        //noinspection unchecked
        return Sequence.of(() -> new Iterator<>() {
            boolean hasNext = true;
            S next = (S) TreeNode.this;

            @Override
            public boolean hasNext() {
                if (hasNext) {
                    return true;
                }
                final var optionalParent = next.optionalParent();
                hasNext = optionalParent.isPresent();
                if (hasNext) {
                    next = optionalParent.orElseThrow();
                }
                return hasNext;
            }

            @Override
            public S next() {
                if (hasNext()) {
                    hasNext = false;
                    return next;
                }
                throw new NoSuchElementException();
            }
        });
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
