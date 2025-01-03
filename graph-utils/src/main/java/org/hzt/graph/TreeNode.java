package org.hzt.graph;

import org.hzt.graph.iterators.GraphIterators;
import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.sequences.Sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * A tree node where the default traversal is breadth first traversal.
 *
 * <p>
 * T (implementing type) and S (child type) must be of same type for this interface to work properly.
 * <p>
 * The iterator that must be implemented, must provide an iterator over the children of the current node
 *
 * @param <N> The type of the node.
 */
@FunctionalInterface
public interface TreeNode<N extends TreeNode<N>> {

    /**
     * @return Returns an iterator that iterates over the children of this tree node
     */
    Iterator<N> childrenIterator();

    default Sequence<N> childrenSequence() {
        return Sequence.of(this::childrenIterator);
    }

    /**
     * @return a sequence of siblings including self
     */
    default Sequence<N> siblingSequence() {
        //noinspection unchecked
        return optionalParent()
                .map(TreeNode::childrenSequence)
                .orElse(Sequence.of((N) this));
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

    default Optional<N> optionalParent() {
        throw new IllegalStateException("optionalParent() is not implemented by default. Override it if you want to use it");
    }

    default Sequence<N> breadthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeBreadthFirstIterator((N) this));
    }

    default Sequence<DepthToTreeNode<N>> breadthFirstDepthTrackingSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeBreadthFirstDepthTrackingIterator((N) this));
    }

    default Sequence<N> depthFirstSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeDepthFirstIterator((N) this));
    }

    default Sequence<DepthToTreeNode<N>> depthFirstDepthTrackingSequence() {
        //noinspection unchecked
        return Sequence.of(() -> GraphIterators.treeNodeDepthFirstDepthTrackingIterator((N) this));
    }

    default boolean isTree() {
        try {
            checkTree(this, new HashSet<>());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return a sequence containing this and the parents of this tree node
     */
    default Sequence<N> parentSequence() {
        //noinspection unchecked
        return Sequence.of(() -> new Iterator<>() {
            boolean hasNext = true;
            N next = (N) TreeNode.this;

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
            public N next() {
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

    default String toTreeString(final Function<? super N, String> toStringFunction) {
        return toTreeString("[", ", ", "]", toStringFunction);
    }

    default String toTreeString(final String opening,
                                final String separator,
                                final String closing,
                                final Function<? super N, String> toStringFunction) {
        final var sb = new StringBuilder();
        toTreeString(this, sb, opening, separator, closing, toStringFunction);
        return sb.toString();
    }

    default String toTreeString(final int indent) {
        return toTreeString(indent, Object::toString);
    }

    default String toTreeString(final int indent, final Function<? super N, String> toStringFunction) {
        return toTreeString(indent, " ", toStringFunction);
    }

    default String toTreeString(final int indent,
                                final String indentString,
                                final Function<? super N, String> toStringFunction) {
        final var sb = new StringBuilder();
        toTreeString(this, sb, 0, indent, indentString, toStringFunction);
        final var sbNoTrailingWhiteSpace = sb.replace(sb.length() - 1, sb.length(), "");
        return sbNoTrailingWhiteSpace.toString();
    }

    default String toBFSTreeString(final int indent) {
        return toBFSTreeString(indent, Object::toString);
    }

    default String toBFSTreeString(final int indent, final Function<? super N, String> toStringFunction) {
        return toBFSTreeString(indent, " ", toStringFunction);
    }

    default String toBFSTreeString(final int indent,
                                   final String indentString,
                                   final Function<? super N, String> toStringFunction) {
        return breadthFirstDepthTrackingSequence()
                .map(n -> indentString.repeat(n.treeDepth() * indent) + toStringFunction.apply(n.node()))
                .joinToString(System.lineSeparator());
    }

    private static <S extends TreeNode<S>> void toTreeString(final TreeNode<S> treeNode,
                                                                   final StringBuilder sb,
                                                                   final int level,
                                                                   final int indent,
                                                                   final String indentString,
                                                                   final Function<? super S, String> toStringFunction) {
        //noinspection unchecked
        sb.append(indentString.repeat(indent * level))
                .append(toStringFunction.apply((S) treeNode))
                .append(System.lineSeparator());
        if (treeNode.childrenSequence().none()) {
            return;
        }
        for (final var child : treeNode.childrenSequence()) {
            toTreeString(child, sb, level + 1, indent, indentString, toStringFunction);
        }
    }

    private static <S extends TreeNode<S>> void toTreeString(final TreeNode<S> treeNode,
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

    private static <S extends TreeNode<S>> void checkTree(
            final TreeNode<S> treeNode,
            final Set<TreeNode<S>> visited
    ) throws Exception {
        visited.add(treeNode);
        for (final var child : treeNode.childrenSequence()) {
            if (visited.contains(child)) {
                throw new Exception();
            } else {
                checkTree(child, visited);
            }
        }
    }
}
