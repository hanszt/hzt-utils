package org.hzt.graph;

import java.util.Collection;

/**
 * @param <N> The type of the node.
 * <p>
 * The iterator that must be implemented, must provide an iterator over the children of the current node
 */
public interface MutableTreeNode<N extends TreeNode<N>> extends TreeNode<N> {

    Collection<N> getMutableChildren();

    N withParent(final N parent);

    default N addChild(final N toAdd) {
        final var children = getMutableChildren();
        children.add(toAdd);
        //noinspection unchecked
        return (N) this;
    }

    default N addChildren(final Iterable<? extends N> toAdd) {
        final var children = getMutableChildren();
        for (final var child : toAdd) {
            children.add(child);
        }
        //noinspection unchecked
        return (N) this;
    }

    default N addChildWithThisAsParent(final N toAdd) {
        final var children = getMutableChildren();
        children.add(toAdd);
        try {
            //noinspection unchecked
            ((MutableTreeNode<N>) toAdd).withParent((N) this);
        } catch (final IllegalStateException e) {
            final var message = "Could not set parent. Override withParent(TreeNode) or try to use addChild(TreeNode) instead...";
            throw new IllegalStateException(message, e);
        }
        //noinspection unchecked
        return (N) this;
    }

    default N addChildrenWithThisAsParent(final Iterable<N> toAdd) {
        final var children = getMutableChildren();
        for (final var child : toAdd) {
            children.add(child);
            //noinspection unchecked
            ((MutableTreeNode<N>) child).withParent((N) this);
        }
        //noinspection unchecked
        return (N) this;
    }

    default N removeSubTree(final N branch) {
        if (!(branch instanceof MutableTreeNode<?> mtn)) {
            throw new IllegalStateException("Branch not instance of MutableTreeNode");
        }
        final var branchChildren = mtn.getMutableChildren();
        for (final var child : branchChildren) {
            if (!child.isLeaf()) {
                //noinspection unchecked
                removeSubTree((N) child);
            }
        }
        branchChildren.removeIf(TreeNode::isLeaf);
        if (branch.isLeaf()) {
            getMutableChildren().removeIf(branch::equals);
        }
        //noinspection unchecked
        return (N) this;
    }
}
