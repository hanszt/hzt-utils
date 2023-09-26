package org.hzt.graph.iterators;

import org.hzt.graph.TreeNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * @param <T> the type of this
 * @param <S> the type of the child
 *
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class TreeNodeBreadthFirstIterator<T, S extends TreeNode<T, S>> implements Iterator<S> {
    private final Queue<S> queue = new LinkedList<>();

    TreeNodeBreadthFirstIterator(final S node) {
        queue.add(node);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public S next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        //removes from front of queue
        final var next = queue.remove();
        next.childrenSequence().forEach(queue::add);
        return next;
    }
}
