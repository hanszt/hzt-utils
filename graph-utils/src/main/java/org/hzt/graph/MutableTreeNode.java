package org.hzt.graph;

import java.util.Collection;

/**
 * @param <T> The type of the node itself
 * @param <S> The type of the children
 * <p>
 * T and S must be of same type for this interface to work properly
 * <p>
 * The iterator that must be implemented, must provide an iterator over the children of the current node
 */
public interface MutableTreeNode<T, S extends TreeNode<T, S>> extends TreeNode<T, S> {

    Collection<S> getMutableChildren();

    S withParent(final S parent);

    default S addChild(final S toAdd) {
        final var children = getMutableChildren();
        children.add(toAdd);
        //noinspection unchecked
        return (S) this;
    }

    default S addChildren(final Iterable<? extends S> toAdd) {
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
            ((MutableTreeNode<T, S>) toAdd).withParent((S) this);
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
            ((MutableTreeNode<T, S>) child).withParent((S) this);
        }
        //noinspection unchecked
        return (S) this;
    }

    default S removeSubTree(final S branch) {
        if (!(branch instanceof MutableTreeNode<?, ?> mtn)) {
            throw new IllegalStateException("Branch not instance of MutableTreeNode");
        }
        final var branchChildren = mtn.getMutableChildren();
        for (final var child : branchChildren) {
            if (!child.isLeaf()) {
                //noinspection unchecked
                removeSubTree((S) child);
            }
        }
        branchChildren.removeIf(TreeNode::isLeaf);
        if (branch.isLeaf()) {
            getMutableChildren().removeIf(branch::equals);
        }
        //noinspection unchecked
        return (S) this;
    }
}
