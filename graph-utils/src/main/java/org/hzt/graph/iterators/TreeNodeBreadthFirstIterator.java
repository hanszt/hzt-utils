package org.hzt.graph.iterators;

import org.hzt.graph.TreeNode;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * @param <T> the type of this
 * @param <S> the type of the child
 *
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class TreeNodeBreadthFirstIterator<T, S extends TreeNode<T, S>> implements Iterator<S> {
    private final Set<S> visited = new HashSet<>();
    private final Queue<S> queue = new LinkedList<>();

    TreeNodeBreadthFirstIterator(final S node) {
        queue.add(node);
        visited.add(node);
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public S next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        //removes from front of queue
        final S next = queue.remove();
        for (final S children : next.getChildren()) {
            if (!this.visited.contains(children)) {
                this.queue.add(children);
                this.visited.add(children);
            }
        }
        return next;
    }
}
