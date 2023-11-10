package org.hzt.graph.iterators;

import org.hzt.graph.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @param <T> the type of this
 * @param <S> the type of the child
 *
 * @see <a href="https://gist.github.com/Xrayez/e67858723beca83f972f5790aae3a26f">BFS and DFS Iterator for Graph</a>
 */
final class TreeNodeDepthFirstIterator<T, S extends TreeNode<T, S>>  implements Iterator<S> {
    private final Set<S> visited = new HashSet<>();
    private final Deque<Iterator<S>> stack = new ArrayDeque<>();
    private S next;

    TreeNodeDepthFirstIterator(final S source) {
        stack.push(source.getChildren().iterator());
        next = source;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public S next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            visited.add(next);
            return next;
        } finally {
            this.advance();
        }
    }

    private void advance() {
        Iterator<S> neighbors = stack.getFirst();
        do {
            while (!neighbors.hasNext()) {  // No more nodes -> back out a level
                stack.pop();
                if (stack.isEmpty()) { // All done!
                    next = null;
                    return;
                }
                neighbors = stack.peek();
            }
            next = neighbors.next();
        } while (visited.contains(next));
        stack.push(next.getChildren().iterator());
    }
}
