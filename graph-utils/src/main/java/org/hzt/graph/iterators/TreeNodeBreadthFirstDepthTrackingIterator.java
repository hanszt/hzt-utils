package org.hzt.graph.iterators;

import org.hzt.graph.TreeNode;
import org.hzt.graph.tuples.DepthToTreeNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @param <T> the type of this
 * @param <S> the type of the child
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class TreeNodeBreadthFirstDepthTrackingIterator<T, S extends TreeNode<T, S>> implements Iterator<DepthToTreeNode<S>> {
    private final Queue<DepthToTreeNode<S>> queue = new LinkedList<>();

    TreeNodeBreadthFirstDepthTrackingIterator(final S node) {
        queue.add(new DepthToTreeNode<>(0, node));
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public DepthToTreeNode<S> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        //removes from front of queue
        final DepthToTreeNode<S> next = queue.remove();
        for (final S child : next.node().childrenSequence()) {
            queue.add(new DepthToTreeNode<>(next.treeDepth() + 1, child));
        }
        return next;
    }
}
