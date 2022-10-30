package org.hzt.graph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public final class GraphAlgorithms {

    private GraphAlgorithms() {
    }

    public static <T> WeightedNode<T> dijkstra(WeightedNode<T> start, WeightedNode<T> goal) {
        start.setCost(0);
        Set<WeightedNode<T>> unsettled = new HashSet<>();
        Set<WeightedNode<T>> settled = new HashSet<>();
        unsettled.add(start);
        while (!unsettled.isEmpty()) {
            WeightedNode<T> current = unsettled.stream()
                    .min(Comparator.comparing(WeightedNode::getCost))
                    .orElseThrow();

            for (WeightedEdge<T> edge : current) {
                WeightedNode<T> neighbor = edge.getOpposite(current);
                if (!settled.contains(neighbor)) {
                    neighbor.setCost(current.getCost());
                    unsettled.add(neighbor);
                }
            }

            if (current.equals(goal)) {
                return current;
            }
            unsettled.remove(current);
            settled.add(current);
        }
        throw new IllegalStateException("Dit not find a path to goal: " + goal);
    }

    public static <T> Node<T> breadthFirstSearch(Node<T> start, Node<T> goal) {
        throw new UnsupportedOperationException();
    }

    public static <T> Node<T> depthFirstSearch(Node<T> start, Node<T> goal) {
        throw new UnsupportedOperationException();
    }
}
