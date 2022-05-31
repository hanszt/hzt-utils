package org.hzt.graph;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class GraphAlgorithmsTest {

    @Test
    @DisplayName("Test find shortest path using Dijkstra")
    void testFindShortestPathUsingDijkstra() {
        final var start = new WeightedNode<>(null, 3);
        final var graph = new WeightedNode<>(null, 2);
        assertThrows(IllegalStateException.class, () -> GraphAlgorithms.dijkstra(start, graph));
    }

    /**
     *    Find the shortest path from A to E
     *    A---3---B---4---E
     *    | \     |     /
     *    2   6   5   7
     *    |     \ | /
     *    C---1---D
     */
    @Disabled("Needs to be fixed")
    @Test
    void testFindShortestPath() {
        Map<String, WeightedNode<String>> graph = new HashMap<>();
        WeightedNode<String> AB = new WeightedNode<>("AB", 3);
        WeightedNode<String> BA = new WeightedNode<>("BA", 3);
        WeightedNode<String> AC = new WeightedNode<>("AC", 2);
        WeightedNode<String> CA = new WeightedNode<>("CA", 2);
        WeightedNode<String> AD = new WeightedNode<>("AD", 6);
        WeightedNode<String> DA = new WeightedNode<>("DA", 6);
        WeightedNode<String> BD = new WeightedNode<>("BD", 5);
        WeightedNode<String> DB = new WeightedNode<>("DB", 5);
        WeightedNode<String> CD = new WeightedNode<>("CD", 1);
        WeightedNode<String> DC = new WeightedNode<>("DC", 1);
        WeightedNode<String> DE = new WeightedNode<>("DE", 7);
        WeightedNode<String> BE = new WeightedNode<>("BE", 4);
        fail("Not implemented");
    }

}
