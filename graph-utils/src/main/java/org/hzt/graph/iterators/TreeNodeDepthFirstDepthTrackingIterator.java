package org.hzt.graph.iterators;

import org.hzt.graph.TreeNode;
import org.hzt.graph.tuples.DepthToTreeNode;
import org.hzt.utils.sequences.Sequence;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @param <T> the type of this
 * @param <S> the type of the child
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class TreeNodeDepthFirstDepthTrackingIterator<T, S extends TreeNode<T, S>> implements Iterator<DepthToTreeNode<S>> {

    private final Deque<Iterator<DepthToTreeNode<S>>> stack = new ArrayDeque<>();
    private DepthToTreeNode<S> next;

    TreeNodeDepthFirstDepthTrackingIterator(final S source) {
        stack.push(source.childrenSequence().map(c -> new DepthToTreeNode<>(1, c)).iterator());
        next = new DepthToTreeNode<>(0, source);
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public DepthToTreeNode<S> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return next;
        } finally {
            this.advance();
        }
    }

    private void advance() {
        Iterator<DepthToTreeNode<S>> children = stack.getFirst();
        while (!children.hasNext()) {  // No more nodes -> back out a level
            stack.pop();
            if (stack.isEmpty()) { // All done!
                next = null;
                return;
            }
            children = stack.peek();
        }
        next = children.next();
        final Sequence<S> newChildren = next.node().childrenSequence();
        final int newTreeDepth = next.treeDepth() + 1;
        stack.push(newChildren.map(c -> new DepthToTreeNode<>(newTreeDepth, c)).iterator());
    }
}
